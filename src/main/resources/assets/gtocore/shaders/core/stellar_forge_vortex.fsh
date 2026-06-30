#version 150

uniform sampler2D DiffuseSampler;
uniform vec4 ColorModulator;
uniform vec2 ScreenSize;
uniform float Time;
uniform float BandAngle;
uniform float DecayRadius;
uniform float DisappearRadius;
uniform float VortexIntensity;

in vec4 vertexColor;
in vec3 localPosition;

out vec4 fragColor;

const int COMPLEXITY = 40;

float hash31(vec3 p) {
    p = fract(p * 0.1031);
    p += dot(p, p.yzx + 33.33);
    return fract((p.x + p.y) * p.z);
}

float vNoise3(vec3 p) {
    vec3 i = floor(p);
    vec3 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);

    float n000 = hash31(i + vec3(0.0, 0.0, 0.0));
    float n100 = hash31(i + vec3(1.0, 0.0, 0.0));
    float n010 = hash31(i + vec3(0.0, 1.0, 0.0));
    float n110 = hash31(i + vec3(1.0, 1.0, 0.0));
    float n001 = hash31(i + vec3(0.0, 0.0, 1.0));
    float n101 = hash31(i + vec3(1.0, 0.0, 1.0));
    float n011 = hash31(i + vec3(0.0, 1.0, 1.0));
    float n111 = hash31(i + vec3(1.0, 1.0, 1.0));

    float nx00 = mix(n000, n100, f.x);
    float nx10 = mix(n010, n110, f.x);
    float nx01 = mix(n001, n101, f.x);
    float nx11 = mix(n011, n111, f.x);
    float nxy0 = mix(nx00, nx10, f.y);
    float nxy1 = mix(nx01, nx11, f.y);
    return mix(nxy0, nxy1, f.z);
}

float fbm(vec3 p) {
    float value = 0.0;
    float amplitude = 0.5;
    for (int i = 0; i < 5; i++) {
        value += amplitude * vNoise3(p);
        p = p * 2.03 + vec3(17.13, 9.27, 3.71);
        amplitude *= 0.5;
    }
    return value;
}

vec3 palette(float t) {
    vec3 a = vec3(0.50, 0.50, 0.50);
    vec3 b = vec3(0.55, 0.48, 0.44);
    vec3 c = vec3(1.00, 1.00, 0.72);
    vec3 d = vec3(0.02, 0.28, 0.55);
    return clamp(a + b * cos(6.28318530718 * (c * t + d)), 0.0, 1.0);
}

void main() {
    vec2 fragPos = gl_FragCoord.xy;
    vec3 p = localPosition;
    float r = length(p);
    if (r <= 0.0001 || r > 1.002) {
        discard;
    }

    float planeRadius = length(p.xz);
    float phi = atan(p.y, max(planeRadius, 0.0001));
    float safeBandAngle = max(BandAngle, 0.001);
    if (abs(phi) > safeBandAngle) {
        discard;
    }

    vec2 planar = p.xz;
    vec2 thetaUnit = planeRadius > 0.0001 ? planar / planeRadius : vec2(1.0, 0.0);
    float actualR = r * DisappearRadius;
    float normalizedPhi = phi / safeBandAngle;

    float edgeNoise = fbm(vec3(thetaUnit * (1.4 + r * 2.0), Time * 0.13));
    float radialFade = 1.0 - smoothstep(DecayRadius, DisappearRadius, actualR);
    float innerFade = actualR < 12 ? 0.0 : smoothstep(0.01, 3.2, actualR);
    float bandAmount = abs(phi) / safeBandAngle;
    float bandFade = 1.0 - smoothstep(0.94 + edgeNoise * 0.06, 1.18, bandAmount);
    float shapeFade = radialFade * innerFade * bandFade;
    if (shapeFade <= 0.00001) {
        discard;
    }

    vec2 whirl = planar * 2.2;
    whirl.x *= 1.08;
    whirl.y += normalizedPhi * 0.42;
    float whirlRadius = length(whirl);
    float whirlAngle = atan(whirl.y, whirl.x);
    float pull = 1.0 - smoothstep(0.03, 2.45, whirlRadius);
    float curlAmount = 0.54 + pull * 1.74;
    vec2 tangent = whirlRadius > 0.0001 ? vec2(-whirl.y, whirl.x) / whirlRadius : vec2(0.0, 0.0);
    whirl += tangent * curlAmount;

    vec2 field = vec2(
        length(whirl) + Time * 0.13 + 0.03 * sin(Time * 0.5),
        atan(whirl.y, whirl.x) + Time * 0.15
    );

    for (int i = 1; i < COMPLEXITY; i++) {
        float fi = float(i);
        vec2 nextField = field + Time * 0.001;
        nextField.x += 0.50 / fi * sin(fi * field.y + Time / 10.0 + 0.20 * fi) + 0.16;
        nextField.y += 0.40 / fi * sin(fi * field.x + Time / 10.0 + 0.30 * (fi + 10.0)) - 0.50;
        field = nextField;
    }

    float anglePhase = 0.16 * sin(field.y) + 0.10 * cos(field.y * 2.0) + 0.04 * sin(whirlAngle * 3.0 - Time * 0.45);
    float phase = 0.17 * field.x + anglePhase;
    vec3 color = palette(phase);
    vec3 hazePosition = vec3(
            field.x * 0.42 + sin(field.y) * 0.18,
            cos(field.y) * 0.42 + sin(field.y * 2.0) * 0.12,
            normalizedPhi * 1.4 + Time * 0.04
    );
    float haze = fbm(hazePosition);
    color = mix(color, palette(phase + 0.22 + haze * 0.08), 0.35);

    float stream = sin(field.x * 7.0 + field.y * 5.0 + haze * 4.0);
    float vein = smoothstep(0.12, 0.92, stream * 0.5 + 0.5);
    color = mix(color, color * color, 0.34 + vein * 0.22);

    float centerShade = smoothstep(0.02, 0.56, whirlRadius);
    float outerShade = 1.0 - smoothstep(1.72, 2.25, whirlRadius);
    float softLight = 0.38 + 0.42 * haze + 0.22 * vein;
    color *= centerShade * outerShade * softLight;

    float centerWell = pow(1.0 - smoothstep(0.02, 0.50, whirlRadius), 1.7);
    color = mix(color, vec3(0.0), centerWell * 0.92);

    float intensity = clamp((0.12 + softLight * 0.68 + vein * 0.18) * shapeFade * VortexIntensity, 0.0, 1.0);
    float alpha = clamp(intensity * vertexColor.a * ColorModulator.a * (0.72 + centerShade * 0.34), 0.0, 0.46);
    if (alpha <= 0.00001) {
        discard;
    }

    vec2 swirlUnit = vec2(cos(whirlAngle), sin(whirlAngle));
    vec2 swirlDirection = normalize(vec2(-swirlUnit.y, swirlUnit.x) + swirlUnit * (haze - 0.5));
    float distortionPixels = (4.0 + 18.0 * pull + 6.0 * vein) * shapeFade * VortexIntensity;
    vec2 sampleUv = clamp((fragPos + swirlDirection * distortionPixels) / ScreenSize, vec2(0.0), vec2(1.0));
    vec2 chromaUv = clamp((fragPos - swirlDirection * distortionPixels * 0.38) / ScreenSize, vec2(0.0), vec2(1.0));

    vec3 sampled = texture(DiffuseSampler, sampleUv).rgb;
    vec3 chroma = texture(DiffuseSampler, chromaUv).rgb;
    sampled = vec3(chroma.r, sampled.g, sampled.b);

    vec3 refracted = mix(sampled, color, 0.58 * intensity) + color * (0.10 + intensity * 0.22);
    fragColor = vec4(refracted * vertexColor.rgb * ColorModulator.rgb, alpha);
}
