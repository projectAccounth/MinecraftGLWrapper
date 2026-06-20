#version 330 core

layout(location = 0) in vec3 Position;
layout(location = 1) in vec4 Color;

layout (std140) uniform Transforms {
    mat4 ProjMat;
    mat4 ViewModelMat;
};

out vec4 vertexColor;
out vec2 TexCoords;

void main() {
    // shit doesn't work let's test later
    vec2 vertices[6] = vec2[](
        vec2(-1.0, -1.0),
        vec2( 1.0, -1.0),
        vec2(-1.0,  1.0),
        
        vec2(-1.0,  1.0),
        vec2( 1.0, -1.0),
        vec2( 1.0,  1.0)
    );

    TexCoords = vertices[gl_VertexID] * 0.5 + 0.5;
    vertexColor = Color;

    gl_Position = ProjMat * ViewModelMat * vec4(Position, 1.0f); vec4(vertices[gl_VertexID], 0.0, 1.0);
}