/* I declare that this code is my own work */
/* Author Precious Ikechukwu cpikechukwu1@sheffield.ac.uk */

import gmaths.*;

import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
import com.jogamp.opengl.util.texture.*;
import com.jogamp.opengl.util.texture.awt.*;
import com.jogamp.opengl.util.texture.spi.JPEGImage;

public class SpinningGlobe {
    private Model globe;
    private Model axis;
    private Model pedestal;
    private float rotationAngle = 0.0f;
    private float lastTime = System.nanoTime() / 1_000_000_000.0f; // Time in seconds

    public SpinningGlobe(GL3 gl, Camera camera, Light light, Texture globeTexture, Texture standTexture) {
        // Create the globe
        globe = makeSphere(gl, camera, light, globeTexture, 1.0f);

        // Create the central axis (smaller sphere)
        axis = makeSphere(gl, camera, light, standTexture, 0.1f);

        // Create the pedestal (cube)
        pedestal = makeCube(gl, camera, light, standTexture, 1.0f);
    }

    private Model makeSphere(GL3 gl, Camera camera, Light light, Texture texture, float scale) {
        Mesh mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
        Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_1t.txt");
        Material material = new Material(new Vec3(1.0f, 1.0f, 1.0f), new Vec3(1.0f, 1.0f, 1.0f), new Vec3(0.1f, 0.1f, 0.1f), 32.0f);
        Model model = new Model("sphere", mesh, new Mat4(1), shader, material, light, camera, texture);
        model.setModelMatrix(Mat4Transform.scale(scale, scale, scale));
        return model;
    }

    private Model makeCube(GL3 gl, Camera camera, Light light, Texture texture, float scale) {
        Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_1t.txt");
        Material material = new Material(new Vec3(0.8f, 0.8f, 0.8f), new Vec3(1.0f, 1.0f, 1.0f), new Vec3(0.1f, 0.1f, 0.1f), 32.0f);
        Model model = new Model("cube", mesh, new Mat4(1), shader, material, light, camera, texture);
        model.setModelMatrix(Mat4Transform.scale(scale, scale / 4, scale));
        return model;
    }

    // Updates the rotation angle for continuous 360 rotation
    private void update(float deltaTime) {
        rotationAngle += deltaTime * 50; // Adjust speed as needed
        Mat4 rotation = Mat4Transform.rotateAroundY(rotationAngle);
        Mat4 translation = Mat4Transform.translate(3f, 3f, 3f);
        Mat4 combinedTransform = Mat4.multiply(translation, rotation);
        Mat4 scaling = Mat4Transform.scale(3f, 3f, 3f);
        Mat4 finishedTransform = Mat4.multiply(combinedTransform, scaling);
        globe.setModelMatrix(finishedTransform);
    }

    public void render(GL3 gl) {
        float currentTime = System.nanoTime() / 1_000_000_000.0f; // Time in seconds
        float deltaTime = currentTime - lastTime;
        lastTime = currentTime;

        // Update rotation
        update(deltaTime);

        // Render the pedestal
        pedestal.setModelMatrix(Mat4.multiply(Mat4Transform.translate(3f, 0.5f, 3f), // Position
                                              Mat4Transform.scale(1f, 1f, 1f)));     // Scale
        pedestal.render(gl);

        // Render the axis
        axis.setModelMatrix(Mat4.multiply(Mat4Transform.translate(3f, 3f, 3f),      // Position
                                          Mat4Transform.scale(0.3f, 5f, 0.3f)));    // Scale (adjust size here)
        axis.render(gl);
        
        globe.render(gl);
    }

    public void dispose(GL3 gl) {
        globe.dispose(gl);
        axis.dispose(gl);
        pedestal.dispose(gl);
    }
}
