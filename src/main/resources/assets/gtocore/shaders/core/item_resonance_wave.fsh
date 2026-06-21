#version 150

uniform sampler2D Sampler0;
uniform vec4 ColorModulator;
uniform float time;
uniform vec2 maskUvMin;
uniform vec2 maskUvMax;
uniform float maskUvShrinkRatio;
uniform vec2 maskFrameSize;
uniform vec4 outlineColor;
uniform vec4 waveColor;
uniform float outlineWidth;
uniform float waveStart;
uniform float waveWidth;
uniform float waveSpacing;
uniform float waveSpeed;
uniform float waveLifetime;
uniform float waveDecay;
uniform float waveStrength;
uniform float maxDistance;
uniform float edgeSoftness;
uniform float alphaCutoff;
uniform float overlayPadding;

in vec2 texCoord0;

out vec4 fragColor;

const int MAX_DISTANCE_STEPS = 12;
const int DIRECTION_COUNT = 16;
const vec2 DIRECTIONS[DIRECTION_COUNT] = vec2[](
    vec2(1.0, 0.0),
    vec2(-1.0, 0.0),
    vec2(0.0, 1.0),
    vec2(0.0, -1.0),
    vec2(0.70710678, 0.70710678),
    vec2(-0.70710678, 0.70710678),
    vec2(0.70710678, -0.70710678),
    vec2(-0.70710678, -0.70710678),
    vec2(0.92387953, 0.38268343),
    vec2(-0.92387953, 0.38268343),
    vec2(0.92387953, -0.38268343),
    vec2(-0.92387953, -0.38268343),
    vec2(0.38268343, 0.92387953),
    vec2(-0.38268343, 0.92387953),
    vec2(0.38268343, -0.92387953),
    vec2(-0.38268343, -0.92387953)
);

bool insideUv(vec2 uv) {
    return uv.x >= 0.0 && uv.y >= 0.0 && uv.x <= 1.0 && uv.y <= 1.0;
}

vec2 toMaskUv(vec2 overlayUv, float padding) {
    float scale = 1.0 + padding * 2.0;
    return overlayUv * scale - vec2(padding);
}

vec2 shrinkMaskUv(vec2 uv) {
    return mix(vec2(0.5), uv, 1.0 - clamp(maskUvShrinkRatio, 0.0, 1.0));
}

vec2 toAtlasUv(vec2 maskUv) {
    return mix(maskUvMin, maskUvMax, shrinkMaskUv(maskUv));
}

float sampleMaskAlpha(vec2 overlayUv, float cutoff, float padding) {
    vec2 maskUv = toMaskUv(overlayUv, padding);
    if (!insideUv(maskUv)) {
        return 0.0;
    }
    float alpha = texture(Sampler0, toAtlasUv(maskUv)).a;
    return alpha >= cutoff ? alpha : 0.0;
}

float findOuterDistance(vec2 uv, vec2 texelSize, float cutoff, float limitPx, float padding) {
    for (int step = 1; step <= MAX_DISTANCE_STEPS; step++) {
        float radius = float(step);
        if (radius > limitPx) {
            break;
        }
        for (int i = 0; i < DIRECTION_COUNT; i++) {
            vec2 sampleUv = uv + DIRECTIONS[i] * texelSize * radius;
            if (sampleMaskAlpha(sampleUv, cutoff, padding) > 0.0) {
                return radius;
            }
        }
    }
    return limitPx + 1.0;
}

void main() {
    float cutoff = clamp(alphaCutoff, 0.001, 0.999);
    float padding = max(overlayPadding, 0.0);
    float centerAlpha = sampleMaskAlpha(texCoord0, cutoff, padding);
    if (centerAlpha >= cutoff) {
        discard;
    }

    vec2 texelSize = 1.0 / ((1.0 + padding * 2.0) * max(maskFrameSize, vec2(1.0)));
    float searchLimit = min(maxDistance, float(MAX_DISTANCE_STEPS));
    float distancePx = findOuterDistance(texCoord0, texelSize, cutoff, searchLimit, padding);
    if (distancePx > searchLimit) {
        discard;
    }

    float softness = max(edgeSoftness, 0.001);
    float outlineAlpha = 1.0 - smoothstep(outlineWidth, outlineWidth + softness, distancePx);
    outlineAlpha *= outlineColor.a;

    float waveAlpha = 0.0;
    float waveDistance = distancePx - (outlineWidth + waveStart);
    if (waveDistance >= 0.0) {
        float spacing = max(waveSpacing, 0.001);
        float speed = max(waveSpeed, 0.001);
        float phase = fract((waveDistance - time * waveSpeed) / spacing);
        float nearestWaveCenter = abs(phase - 0.5) * spacing;
        float band = 1.0 - smoothstep(waveWidth, waveWidth + softness, nearestWaveCenter);
        float pulseAge = waveDistance / speed;
        float lifetime = max(waveLifetime, 0.001);
        float lifetimeFadeStart = max(lifetime - 0.15, 0.0);
        float lifetimeFade = 1.0 - smoothstep(lifetimeFadeStart, lifetime, pulseAge);
        float decay = exp(-max(waveDecay, 0.0) * pulseAge);
        waveAlpha = band * lifetimeFade * decay * waveStrength * waveColor.a;
    }

    float alpha = clamp(outlineAlpha + waveAlpha, 0.0, 1.0);
    if (alpha <= 0.001) {
        discard;
    }

    vec3 color = outlineColor.rgb * outlineAlpha + waveColor.rgb * waveAlpha;
    color /= max(alpha, 0.0001);
    fragColor = vec4(color * ColorModulator.rgb, alpha * ColorModulator.a);
}
