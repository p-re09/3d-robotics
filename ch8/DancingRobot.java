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

public class DancingRobot {

    private Camera camera;
    private Light light;
    private Model sphere;
    private Material material;

    private SGNode robotRoot;
    private TransformNode robotMoveTranslate;
    private TransformNode sphere1Rotate, sphere2Rotate, sphere3Rotate, headRotate;
    
    // Arm transform nodes
    private TransformNode leftArmRotate, rightArmRotate;
    private boolean isDancing = false;

    // Eye transform nodes
    private TransformNode leftEyeRotate, rightEyeRotate;

    // Antenna transform node
    private TransformNode antennaRotate;

    private float rotationAngle = 0.0f;

    public DancingRobot(GL3 gl, Camera camera, Light light, Texture t1) {
        this.camera = camera;
        this.light = light;
        this.sphere = makeSphere(gl, t1);

        buildRobot();
    }

    // Creating robot components
    private void buildRobot() {
        robotRoot = new NameNode("robot root");
        robotMoveTranslate = new TransformNode("robot translate", Mat4Transform.translate(0, 0, 0));

        // Sphere 1 (Base Layer)
        sphere1Rotate = new TransformNode("sphere1 rotate", Mat4Transform.rotateAroundX(0)); // No initial rotation
        NameNode sphere1 = makeSphereLayer("sphere1", Mat4Transform.scale(0.4f, 1.5f, 0.4f), Mat4Transform.translate(0, 0.5f, 0));

        // Sphere 2 (Middle Layer)
        sphere2Rotate = new TransformNode("sphere2 rotate", Mat4Transform.translate(0, 0, 0)); // No initial rotation
        NameNode sphere2 = makeSphereLayer("sphere2", Mat4Transform.scale(0.4f, 1.5f, 0.4f), Mat4Transform.translate(0, 1.5f, 0));

        // Sphere 3 (Top Layer)
        sphere3Rotate = new TransformNode("sphere3 rotate", Mat4Transform.translate(0, 0, 0)); // No initial rotation
        NameNode sphere3 = makeSphereLayer("sphere3", Mat4Transform.scale(0.4f, 1.5f, 0.4f), Mat4Transform.translate(0, 2.5f, 0));

        // Head
        headRotate = new TransformNode("head rotate", Mat4Transform.translate(0, 0f, 0)); // No initial rotation
        NameNode head = makeSphereLayer("head", Mat4Transform.scale(1.5f, 1.5f, 1.5f), Mat4Transform.translate(0, 3.5f, 0));

        // Left Arm
        leftArmRotate = new TransformNode("left arm rotate", Mat4Transform.translate(0, 0, 0)); // No initial rotation
        NameNode leftArm = makeSphereLayer("leftArm", Mat4Transform.scale(2f, 0.4f, 0.4f), Mat4Transform.translate(-0.6f, 9.5f, 0));  // Positioned to the left of sphere3

        // Right Arm
        rightArmRotate = new TransformNode("right arm rotate", Mat4Transform.translate(0, 0, 0)); // No initial rotation
        NameNode rightArm = makeSphereLayer("rightArm", Mat4Transform.scale(2f, 0.4f, 0.4f), Mat4Transform.translate(0.6f, 9.5f, 0)); // Positioned to the right of sphere3

        // Left Eye
        leftEyeRotate = new TransformNode("left eye rotate", Mat4Transform.translate(-0.2f, 0, 0)); // No initial rotation
        NameNode leftEye = makeSphereLayer("leftEye", Mat4Transform.scale(0.2f, 0.2f, 0.2f), Mat4Transform.translate(-0.4f, 25f, 3.6f));  // Positioned to the left side of the head

        // Right Eye
        rightEyeRotate = new TransformNode("right eye rotate", Mat4Transform.translate(0.2f, 0, 0)); // No initial rotation
        NameNode rightEye = makeSphereLayer("rightEye", Mat4Transform.scale(0.2f, 0.2f, 0.2f), Mat4Transform.translate(0.4f, 25f, 3.6f));  // Positioned to the right side of the head

        // Antenna
        antennaRotate = new TransformNode("antenna rotate", Mat4Transform.translate(0, 0, 0)); // Positioned above the head
        NameNode antenna = makeSphereLayer("antenna", Mat4Transform.scale(0.2f, 0.8f, 0.2f), Mat4Transform.translate(0, 8f, 0)); // Small sphere for antenna

        // Assembling the robot
        robotRoot.addChild(robotMoveTranslate);
        robotMoveTranslate.addChild(sphere1Rotate);
        sphere1Rotate.addChild(sphere1);
        sphere1.addChild(sphere2Rotate);
        sphere2Rotate.addChild(sphere2);
        sphere2.addChild(sphere3Rotate);
        sphere3Rotate.addChild(sphere3);
        sphere3.addChild(headRotate);
        headRotate.addChild(head);

        // Add the arms to the top sphere (sphere3)
        sphere3.addChild(leftArmRotate);
        leftArmRotate.addChild(leftArm);
        sphere3.addChild(rightArmRotate);
        rightArmRotate.addChild(rightArm);

        // Add the eyes to the head (head)
        head.addChild(leftEyeRotate);
        leftEyeRotate.addChild(leftEye);
        head.addChild(rightEyeRotate);
        rightEyeRotate.addChild(rightEye);

        // Add the antenna to the head (head)
        head.addChild(antennaRotate);
        antennaRotate.addChild(antenna);

        robotRoot.update(); // Finalize the transformations
    }

    private NameNode makeSphereLayer(String name, Mat4 scale, Mat4 translate) {
        NameNode node = new NameNode(name);
        TransformNode scaleNode = new TransformNode(name + " scale", scale);
        TransformNode translateNode = new TransformNode(name + " translate", translate);
        ModelNode modelNode = new ModelNode(name + " shape", sphere);

        node.addChild(scaleNode);
        scaleNode.addChild(translateNode);
        translateNode.addChild(modelNode);
        return node;
    }

    private Model makeSphere(GL3 gl, Texture t1) {
        Mesh mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
        Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_1t.txt");
        Material material = new Material(new Vec3(0.8f, 0.2f, 0.2f), new Vec3(0.8f, 0.2f, 0.2f), new Vec3(0.2f, 0.2f, 0.2f), 32.0f);
        return new Model("sphere", mesh, new Mat4(1), shader, material, light, camera, t1);
    }
    public void startDancing() {
        isDancing = true;
    }

    public void stopDancing() {
        isDancing = false;
    }
    public void toggleDancing() {
        isDancing = !isDancing;
    }

    public boolean isDancing(){
        return isDancing;
    }

    public void reduceBrightness(float reducingFactor) {
        reducingFactor = Math.max(0f, Math.min(reducingFactor, 1f));

        material.setAmbient(
            material.getAmbient().x * reducingFactor, material.getAmbient().z * reducingFactor, 0.0f
        );
    }


    float swayAngle = 0.0f;
    float swaySpeed = 25.0f;
    float maxSwayAngle = 15.0f;

    // Updates the rotation angle for continuous swaying-like motion
    public void update(float deltaTime) {
        swayAngle += deltaTime * 5f;
        float rotation = (float) Math.sin(swayAngle) * maxSwayAngle;
        Mat4 combinedTransform = Mat4.multiply(Mat4Transform.translate(-1.8f, 0, -1.5f), Mat4Transform.rotateAroundX(rotation));
        robotMoveTranslate.setTransform(combinedTransform);
        robotRoot.update(); // Update the scene graph with new transforms
    }

    private float lastTime = System.nanoTime() / 1000000.0f;

    public void render(GL3 gl) {
        float currentTime = System.nanoTime() / 1000000.0f;
        float deltaTime = (currentTime - lastTime) / 1000.0f;
        lastTime = currentTime;
        update(deltaTime);
        robotRoot.draw(gl);
    }

    public void dispose(GL3 gl) {
        sphere.dispose(gl);
    }
}
