#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;
uniform vec4 ColorModulator;
uniform vec2 ScreenSize;
uniform vec2 BlackHoleCenterScreen;
uniform float ShadowRadiusScreen;
uniform float LensingRadiusScreen;
uniform float BlackHoleFrontDepth;
uniform float LensStrength;
uniform float PhotonRingWidth;

in vec4 vertexColor;

out vec4 fragColor;

void main() {
    if (!gl_FrontFacing) {
        discard;
    }

    vec2 fragPos = gl_FragCoord.xy;
    vec2 screenUv = fragPos / ScreenSize;
    vec2 fromCenter = fragPos - BlackHoleCenterScreen;
    float distanceToCenter = length(fromCenter);

    if (distanceToCenter >= LensingRadiusScreen) {
        discard;
    }

    float sceneDepth = texture(DepthSampler, screenUv).r;
    if (sceneDepth < gl_FragCoord.z - 0.0005) {
        discard;
    }

    vec2 direction = distanceToCenter > 0.0 ? normalize(fromCenter) : vec2(0.0, 0.0);
    float band = max(LensingRadiusScreen - ShadowRadiusScreen, 0.0001);
    float lensT = clamp((distanceToCenter - ShadowRadiusScreen) / band, 0.0, 1.0);
    float outerFade = 1.0 - smoothstep(0.72, 1.0, lensT);
    float nearShadow = 1.0 - smoothstep(0.0, 0.82, lensT);
    float critical = exp(-abs(distanceToCenter - ShadowRadiusScreen) / max(ShadowRadiusScreen * PhotonRingWidth, 1.0));
    float bend = clamp((nearShadow * nearShadow * 1.18 + critical * 0.55) * LensStrength, 0.0, 1.0);

    float radialScale = mix(1.0, -0.62, bend);
    vec2 sampleUv = clamp((BlackHoleCenterScreen + direction * distanceToCenter * radialScale) / ScreenSize, vec2(0.0), vec2(1.0));
    float sampleDepth = texture(DepthSampler, sampleUv).r;
    if (sampleDepth < min(gl_FragCoord.z, BlackHoleFrontDepth) - 0.0005) {
        discard;
    }

    vec4 sampled = texture(DiffuseSampler, sampleUv);
    float ring = critical * outerFade;
    vec3 ringColor = vec3(1.0, 0.82, 0.48) * ring * 0.42;
    float alpha = max(outerFade * bend * 0.82, ring * 0.95);
    fragColor = vec4(sampled.rgb * vertexColor.rgb * ColorModulator.rgb + ringColor, alpha * vertexColor.a * ColorModulator.a);
}
