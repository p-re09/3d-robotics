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

    private TransformNode robotMoveTranslate,
                          sphere1Rotate, 
                          sphere2Rotate, 
                          sphere3Rotate, 
                          headRotate, 
                          antennaRotate,
                          leftArmRotate,
                          rightArmRotate,
                          leftEyeRotate,
                          rightEyeRotate;

    private boolean isDancing = false;

    private float rotationAngle = 0.0f;

    public DancingRobot(GL3 gl, Camera camera, Light light, Texture t1) {
        System.out.println("DancingRobot NEW CODE running: " + System.currentTimeMillis());
        this.camera = camera;
        this.light = light;
        this.sphere = makeSphere(gl, t1);

        buildRobot();
    }
    
    private static class Part {
        NameNode root;
        TransformNode attach; // where children will be connected
        Part(NameNode root, TransformNode attach) {
            this.root = root;
            this.attach = attach;
        }
    }

    private void buildRobot() {

        robotRoot = new NameNode("robot root");
        robotMoveTranslate = new TransformNode("robot translate", Mat4Transform.translate(-1.8f, 0, -1.5f));

        // Root attachment
        robotRoot.addChild(robotMoveTranslate);

        Part sphere1 = makeSpherePart("sphere1",
            Mat4Transform.scale(0.4f, 1.5f, 0.4f),
            Mat4Transform.translate(0, 0.8f, 0)
        );

        Part sphere2 = makeSpherePart("sphere2",
            Mat4Transform.scale(0.4f, 1.5f, 0.4f),
            Mat4Transform.translate(0, 1.2f, 0)   // relative step
        );

        Part sphere3 = makeSpherePart("sphere3",
            Mat4Transform.scale(0.4f, 1.5f, 0.4f),
            Mat4Transform.translate(0, 1.2f, 0)   // relative step
        );

        // Insert a rotation node between the robot's world translation and sphere1
        sphere1Rotate = new TransformNode("sphere1 rotate", Mat4Transform.rotateAroundZ(0));

        robotMoveTranslate.addChild(sphere1Rotate);
        sphere1Rotate.addChild(sphere1.root);

        // Continue the true local stacking via attach points
        sphere2Rotate = new TransformNode("sphere2 rotate", Mat4Transform.rotateAroundZ(0));

        sphere1.attach.addChild(sphere2Rotate);
        sphere2Rotate.addChild(sphere2.root);
        
        sphere3Rotate = new TransformNode("sphere3 rotate", Mat4Transform.rotateAroundZ(0));
        sphere2.attach.addChild(sphere3Rotate);
        sphere3Rotate.addChild(sphere3.root);


        // --- Head (attached to sphere3, and head will have children so use Part) ---
        Part head = makeSpherePart("head",
            Mat4Transform.scale(1.5f, 1.5f, 1.5f),
            Mat4Transform.translate(0, 1.3f, 0)   // local offset from sphere3 top area
        );
        headRotate = new TransformNode("head rotate", Mat4Transform.rotateAroundZ(0));

        sphere3.attach.addChild(headRotate);
        headRotate.addChild(head.root);

        // --- Arms (attached to sphere3) ---
        Part leftArm = makeSpherePart("leftArm",
            Mat4Transform.scale(1.2f, 0.25f, 0.25f),
            Mat4Transform.translate(-0.9f, 0.6f, 0f)
        );
        leftArmRotate = new TransformNode("leftArm rotate", Mat4Transform.rotateAroundZ(0));

        sphere3.attach.addChild(leftArmRotate);
        leftArmRotate.addChild(leftArm.root);

        Part rightArm = makeSpherePart("rightArm",
            Mat4Transform.scale(1.2f, 0.25f, 0.25f),
            Mat4Transform.translate(0.9f, 0.6f, 0f)
        );
        rightArmRotate = new TransformNode("rightArm rotate", Mat4Transform.rotateAroundZ(0));

        sphere3.attach.addChild(rightArmRotate);
        rightArmRotate.addChild(rightArm.root);

        // --- Eyes (attached to head) ---
        Part leftEye = makeSpherePart("leftEye",
            Mat4Transform.scale(0.2f, 0.2f, 0.2f),
            Mat4Transform.translate(-0.45f, 0.25f, 0.95f)
        );
        leftEyeRotate = new TransformNode("leftEye rotate", Mat4Transform.rotateAroundY(0));

        head.attach.addChild(leftEyeRotate);
        leftEyeRotate.addChild(leftEye.root);

        Part rightEye = makeSpherePart("rightEye",
            Mat4Transform.scale(0.2f, 0.2f, 0.2f),
            Mat4Transform.translate(0.45f, 0.25f, 0.95f)
        );
        rightEyeRotate = new TransformNode("rightEye rotate", Mat4Transform.rotateAroundY(0));

        head.attach.addChild(rightEyeRotate);
        rightEyeRotate.addChild(rightEye.root);

        // --- Antenna (attached to head) ---
        Part antenna = makeSpherePart("antenna",
            Mat4Transform.scale(0.15f, 0.7f, 0.15f),
            Mat4Transform.translate(0f, 1.0f, 0f)
        );
        antennaRotate = new TransformNode("antenna rotate", Mat4Transform.rotateAroundX(0));

        head.attach.addChild(antennaRotate);
        antennaRotate.addChild(antenna.root);

        robotRoot.update();
    }


    private Part makeSpherePart(String name, Mat4 scale, Mat4 translate) {
        NameNode node = new NameNode(name);
        TransformNode scaleNode = new TransformNode(name + " scale", scale);
        TransformNode translateNode = new TransformNode(name + " translate", translate);
        TransformNode attachNode = new TransformNode(name + " attach", Mat4Transform.translate(0, 0, 0));
        ModelNode modelNode = new ModelNode(name + " shape", sphere);

        node.addChild(translateNode);
        translateNode.addChild(scaleNode);
        scaleNode.addChild(modelNode);
        translateNode.addChild(attachNode);
        return new Part(node, attachNode);
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

    /*private void animateParts(float time) {

    // Base spheres (slow twist)
    sphere1Rotate.setTransform(Mat4Transform.rotateAroundY((float)Math.sin(time) * 5f));
    sphere2Rotate.setTransform(Mat4Transform.rotateAroundY((float)Math.sin(time + 1) * 8f));
    sphere3Rotate.setTransform(Mat4Transform.rotateAroundY((float)Math.sin(time + 2) * 10f));

    // Head nodding
    headRotate.setTransform(Mat4Transform.rotateAroundX((float)Math.sin(time * 1.5f) * 10f));

    // Arms swinging
    leftArmRotate.setTransform(Mat4Transform.rotateAroundZ((float)Math.sin(time * 2f) * 30f));
    rightArmRotate.setTransform(Mat4Transform.rotateAroundZ((float)-Math.sin(time * 2f) * 30f));

    // Eyes subtle movement
    leftEyeRotate.setTransform(Mat4Transform.rotateAroundY((float)Math.sin(time * 3f) * 5f));
    rightEyeRotate.setTransform(Mat4Transform.rotateAroundY((float)-Math.sin(time * 3f) * 5f));

    // Antenna wiggle
    antennaRotate.setTransform(Mat4Transform.rotateAroundZ((float)Math.sin(time * 4f) * 20f));
    }*/


    float swayAngle = 0.0f;
    float swaySpeed = 25.0f;
    float maxSwayAngle = 15.0f;

    public void update(float deltaTime) {
        // Static robot for Increment 0
        swayAngle += deltaTime * 2.0f; // speed 

        float sway = (float)Math.sin(swayAngle) * maxSwayAngle;
        sphere1Rotate.setTransform(Mat4Transform.rotateAroundZ(-sway));
        sphere2Rotate.setTransform(Mat4Transform.rotateAroundZ(sway));
        sphere3Rotate.setTransform(Mat4Transform.rotateAroundZ(-sway));
        headRotate.setTransform(Mat4Transform.rotateAroundZ(sway));
        leftArmRotate.setTransform(Mat4Transform.rotateAroundZ(sway));
        rightArmRotate.setTransform(Mat4Transform.rotateAroundZ(-sway));
        leftEyeRotate.setTransform(Mat4Transform.rotateAroundY(-sway));
        rightEyeRotate.setTransform(Mat4Transform.rotateAroundY(sway));
        antennaRotate.setTransform(Mat4Transform.rotateAroundX(-sway));

        robotRoot.update();
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
