#version 150

uniform sampler2D DiffuseSampler;
uniform vec4 ColorModulator;
uniform vec2 ScreenSize;
uniform vec2 EffectCenterScreen;
uniform float EffectRadiusScreen;
uniform float PatternRadiusScreen;
uniform float Time;
uniform float OverlayStrength;

in vec4 vertexColor;

out vec4 fragColor;

#define MAX_ITER 4

float safeDivisor(float value) {
    return abs(value) < 0.0001 ? (value < 0.0 ? -0.0001 : 0.0001) : value;
}

vec3 timeBasedColor() {
    float hue = mod(Time * 0.1, 1.0);
    float r = abs(hue * 6.0 - 3.0) - 1.0;
    float g = 2.0 - abs(hue * 6.0 - 2.0);
    float b = 2.0 - abs(hue * 6.0 - 4.0);
    return clamp(vec3(r, g, b), 0.0, 1.0);
}

void main() {
    vec2 fragPos = gl_FragCoord.xy;
    vec2 sampleUv = clamp(fragPos / ScreenSize, vec2(0.0), vec2(1.0));
    vec4 sampled = texture(DiffuseSampler, sampleUv);

    float effectRadius = max(EffectRadiusScreen, 1.0);
    float patternRadius = max(PatternRadiusScreen, 1.0);
    vec2 fromCenter = fragPos - EffectCenterScreen;
    vec2 surfacePosition = fromCenter / patternRadius * 0.5 + vec2(0.5);
    vec2 p0 = surfacePosition * 5.0 - vec2(10.0);
    float rotateSpeed = 0.5 * OverlayStrength;
    float scSqTime = Time * Time * 0.1;
    vec2 p = vec2(p0.x * cos(scSqTime * rotateSpeed) - p0.y * sin(scSqTime * rotateSpeed), p0.x * sin(scSqTime * rotateSpeed) + p0.y * cos(scSqTime * rotateSpeed));
    vec2 i = p;
    float c = 1.0;
    float inten = 0.01;

    for (int n = 0; n < MAX_ITER; n++) {
        float t = Time * (1.0 - (3.0 / float(n + 1)));
        i = p + vec2(cos(t - i.x) + sin(t + i.y), sin(t - i.y) + cos(t + i.x));
        vec2 warped = vec2(
            p.x * inten / safeDivisor(sin(i.x + t)),
            p.y * inten / safeDivisor(cos(i.y + t))
        );
        c += 1.0 / max(length(warped), 0.0001);
    }

    c /= float(MAX_ITER);
    c = 1.5 - sqrt(max(c, 0.0));
    float glow = clamp(c * c * c * c, 0.0, 1.0);
    float pulse = 0.72 + 0.28 * sin(Time * 1.7 + length(p) * 0.35);
    float radialFade = 1.0 - smoothstep(effectRadius * 0.92, effectRadius, length(fromCenter));
    float intensity = clamp((0.18 + glow * 0.82) * pulse * OverlayStrength * radialFade, 0.0, 1.0);
    float alpha = intensity * vertexColor.a * ColorModulator.a;

    vec3 overlayColor = mix(timeBasedColor(), vec3(1.0, 1.0, 1.0), glow);
    vec3 color = sampled.rgb + overlayColor * intensity;
    fragColor = vec4(color * vertexColor.rgb * ColorModulator.rgb, alpha);
}
