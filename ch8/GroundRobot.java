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

public class GroundRobot {

    private Camera camera;
    private Light light;
    private Model cube, sphere;

    private SGNode robotRoot;
    private TransformNode baseRotate, antennaRotate, lampRotate, bulbRotate;
    private Light lampLight;

    public GroundRobot(GL3 gl, Camera camera, Light light, Texture cubeTexture, Texture sphereTexture) {
        this.camera = camera;
        this.light = light;
        this.cube = makeCube(gl, cubeTexture);
        this.sphere = makeSphere(gl, sphereTexture);
        this.lampLight = new Light(gl);
        this.lampLight.setCamera(camera);

        buildRobot();
    }

    // Creating robot components
    private void buildRobot() {
        // Root transformation to move the robot along the X-axis
        TransformNode rootTranslate = new TransformNode("root translate", Mat4Transform.translate(5.0f, 0.0f, 0.0f)); // Move 5 units along X
        // Root node
        robotRoot = new NameNode("robot root");
        rootTranslate.addChild(robotRoot);

        // Base (Cube)
        TransformNode baseTransform = new TransformNode("base transform", Mat4Transform.translate(0, 0.5f, 0));
        NameNode base = makeLayer("base", Mat4Transform.scale(2f, 2f, 2f), Mat4Transform.translate(0, 0.5f, 0), cube);

        // Antenna (Sphere)
        antennaRotate = new TransformNode("antenna rotate", Mat4Transform.translate(0, 1f, 0));
        NameNode antenna = makeLayer("antenna", Mat4Transform.scale(0.4f, 3f, 0.4f), Mat4Transform.translate(0, 2f, 0), sphere);

        // Lamp components
        lampRotate = new TransformNode("lamp rotate", Mat4Transform.rotateAroundY(0));
        TransformNode lampTransform = new TransformNode("lamp transform", Mat4Transform.translate(0, 0.5f, 0));

        // Bulb rotation node
        bulbRotate = new TransformNode("bulb rotate", Mat4Transform.rotateAroundY(0));
        lampTransform.addChild(bulbRotate);

        // Bulb
        NameNode bulb = makeLayer("bulb", Mat4Transform.scale(0.2f, 0.4f, 0.5f), Mat4Transform.translate(0, 3f, 0.3f), sphere);
        bulbRotate.addChild(bulb);

        // Casing
        NameNode casing = makeLayer("casing", Mat4Transform.scale(0.2f, 0.2f, 0.2f), Mat4Transform.translate(0, 3f, 0), sphere);
        lampTransform.addChild(casing);

        // Building the scene graph
        rootTranslate.addChild(robotRoot);
        robotRoot.addChild(baseTransform);
        baseTransform.addChild(base);

        base.addChild(antennaRotate);
        antennaRotate.addChild(antenna);

        antenna.addChild(lampRotate);
        lampRotate.addChild(lampTransform);

        robotRoot.update(); // Finalize transformations
    }

    private NameNode makeLayer(String name, Mat4 transform, Mat4 scale, Model model) {
        NameNode node = new NameNode(name);
        TransformNode transformNode = new TransformNode(name + " transform", transform);
        TransformNode scaleNode = new TransformNode(name + " scale", scale);
        ModelNode modelNode = new ModelNode(name + " model", model);

        node.addChild(scaleNode);
        scaleNode.addChild(transformNode);
        transformNode.addChild(modelNode);
        return node;
    }

    private Model makeCube(GL3 gl, Texture texture) {
        Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_1t.txt");
        Material material = new Material(new Vec3(0.8f, 0.8f, 0.8f), new Vec3(1.0f, 1.0f, 1.0f), new Vec3(0.1f, 0.1f, 0.1f), 32.0f);
        return new Model("cube", mesh, new Mat4(1), shader, material, light, camera, texture);
    }

    private Model makeSphere(GL3 gl, Texture texture) {
        Mesh mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
        Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_1t.txt");
        Material material = new Material(new Vec3(1.0f, 1.0f, 1.0f), new Vec3(1.0f, 1.0f, 1.0f), new Vec3(0.1f, 0.1f, 0.1f), 32.0f);
        return new Model("sphere", mesh, new Mat4(1), shader, material, light, camera, texture);
    }

    private float bulbRotationAngle = 0.0f;

    // Rotates bulb along y axis
    private void rotateBulb(float deltaTime) {
        float rotationSpeed = 40f; // degrees per second
        bulbRotationAngle += deltaTime * rotationSpeed;
        bulbRotationAngle %= 360;
        bulbRotate.setTransform(Mat4Transform.rotateAroundY(bulbRotationAngle)); // Rotate bulb
        robotRoot.update();
    }

    public void update(float deltaTime) {
        rotateBulb(deltaTime); // Only rotates the bulb
    }

    private float lastTime = System.nanoTime() / 1000000.0f;

    public void render(GL3 gl) {
        float currentTime = System.nanoTime() / 1000000.0f;
        float deltaTime = (currentTime - lastTime) / 1000.0f;
        lastTime = currentTime;
        update(deltaTime);
        
        lampLight.render(gl); // Render light emitted from the lamp
        robotRoot.draw(gl);
    }

    public void dispose(GL3 gl) {
        cube.dispose(gl);
        sphere.dispose(gl);
        lampLight.dispose(gl);
    }
}
