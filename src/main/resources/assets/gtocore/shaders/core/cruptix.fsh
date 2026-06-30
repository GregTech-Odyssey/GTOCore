#version 150

uniform sampler2D Sampler0;
uniform vec4 ColorModulator;
uniform vec2 maskTextureSize;
uniform vec2 maskViewportOrigin;
uniform float time;
uniform vec2 resolution;
uniform vec2 mousePos;

in vec2 texCoord0;

out vec4 fragColor;

#define NUM_OCTAVES 6

mat3 rotX(float a) {
    float c = cos(a);
    float s = sin(a);
    return mat3(
        1.0, 0.0, 0.0,
        0.0, c, -s,
        0.0, s, c
    );
}

mat3 rotY(float a) {
    float c = cos(a);
    float s = sin(a);
    return mat3(
        c, 0.0, -s,
        0.0, 1.0, 0.0,
        s, 0.0, c
    );
}

float randomValue(vec2 pos) {
    return fract(sin(dot(pos.xy, vec2(13.9898, 78.233))) * 43758.5453123);
}

float noiseValue(vec2 pos) {
    vec2 i = floor(pos);
    vec2 f = fract(pos);
    float a = randomValue(i + vec2(0.0, 0.0));
    float b = randomValue(i + vec2(1.0, 0.0));
    float c = randomValue(i + vec2(0.0, 1.0));
    float d = randomValue(i + vec2(1.0, 1.0));
    vec2 u = f * f * (3.0 - 2.0 * f);
    return mix(a, b, u.x) + (c - a) * u.y * (1.0 - u.x) + (d - b) * u.x * u.y;
}

float fbm(vec2 pos) {
    float value = 0.0;
    float amplitude = 0.5;
    vec2 shift = vec2(100.0);
    mat2 rot = mat2(cos(0.5), sin(0.5), -sin(0.5), cos(0.5));
    for (int i = 0; i < NUM_OCTAVES; i++) {
        float dir = mod(float(i), 2.0) > 0.5 ? 1.0 : -1.0;
        value += amplitude * noiseValue(pos - 0.25 * dir * time);
        pos = rot * pos * 2.0 + shift;
        amplitude *= 0.5;
    }
    return value;
}

vec2 screenMaskUv() {
    return (gl_FragCoord.xy - maskViewportOrigin) / max(maskTextureSize, vec2(1.0));
}

vec4 sampleMask() {
    return texture(Sampler0, screenMaskUv());
}

void main() {
    vec4 mask = sampleMask();
    if (mask.a < 0.001) {
        discard;
    }

    vec2 fragCoord = (texCoord0 - mousePos / resolution * 4) * resolution.xy;
    vec2 p = (fragCoord * 3.0 - resolution.xy) / min(resolution.x, resolution.y);
    p -= vec2(12.0, 0.0);

    float time2 = 1.0;
    vec2 q = vec2(0.0);
    q.x = fbm(p + 0.00 * time2);
    q.y = fbm(p + vec2(1.0));

    vec2 r = vec2(0.0);
    r.x = fbm(p + 1.0 * q + vec2(1.7, 1.2) + 0.15 * time2);
    r.y = fbm(p + 1.0 * q + vec2(8.3, 2.8) + 0.126 * time2);

    float f = fbm(p + r);

    vec3 color = mix(
        vec3(1.0, 1.0, 2.0),
        vec3(1.0, 1.0, 1.0),
        clamp((f * f) * 5.5, 1.2, 15.5)
    );

    color = mix(
        color,
        vec3(1.0, 1.0, 1.0),
        clamp(length(q), 2.0, 2.0)
    );

    color = mix(
        color,
        vec3(0.3, 0.2, 1.0),
        clamp(length(r.x), 0.0, 5.0)
    );

    color = (f * f * f + 0.9 * f) * color;

    vec2 uv = fragCoord / resolution.xy;
    float alpha = 50.0 - max(pow(100.0 * distance(uv.x, -1.0), 0.0), pow(2.0 * distance(uv.y, 0.5), 5.0));
    alpha = clamp(alpha * color.r, 0.0, 1.0);

    fragColor = vec4(color * ColorModulator.rgb * 0.8 + mask.rgb * 0.2, alpha * mask.a * ColorModulator.a);
}
