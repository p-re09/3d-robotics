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

public class Room {

  private ModelMultipleLights[] wall;
  private Camera camera;
  private Light[] lights;
  private Texture t0, t1, t2, t3, t4, t5;
  private float size = 16f;

  public Room(GL3 gl, Camera c, Light[] l, Texture t0, Texture t1, Texture t2, Texture t3, Texture t4, Texture t5) {
    camera = c;
    lights = l;
    this.t0 = t0;
    this.t1 = t1;
    this.t2 = t2;
    this.t3 = t3;
    this.t4 = t4;
    this.t5 = t5;
    wall = new ModelMultipleLights[11];
    wall[0] = makeWall0(gl);
    wall[1] = makeWall1(gl);
    wall[2] = makeWall2(gl);
    wall[3] = makeWall3(gl);
    wall[4] = makeWall4(gl);
    wall[5] = makeWall5(gl);
    wall[6] = makeWall6(gl);
    wall[7] = makeWall7(gl);
    wall[8] = makeWall8(gl);
    wall[9] = makeWall9(gl);
  }
 
  // Creating the walls
  private ModelMultipleLights makeWall0(GL3 gl) {
    String name="floor";
    Vec3 basecolor = new Vec3(0.5f, 0.5f, 0.5f); // grey
    Material material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    //create floor
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
    ModelMultipleLights model = new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, t3);
    return model;
  }

  private ModelMultipleLights makeWall1(GL3 gl) {
    String name="back_wall";
    Vec3 basecolor = new Vec3(0.5f, 0.5f, 0.5f); // grey
    Material material = new Material(new Vec3(0.1f, 0.1f, 0.1f), new Vec3(0.7f, 0.7f, 0.7f), new Vec3(1.0f, 1.0f, 1.0f), 32.0f);
    // back wall
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0,size*0.5f,-size*0.5f), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_2t.txt");
    ModelMultipleLights model = new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, t0, t1);
    return model;
  }

  private ModelMultipleLights makeWall2(GL3 gl) {
    String name="left_wall";
    Material material = new Material(new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    // left wall
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,size,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-size*0.5f,size*0.5f,0), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices3.clone(), TwoTriangles.indices2.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_0t.txt");
    ModelMultipleLights model = new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera);
    return model;
  }

  private ModelMultipleLights makeWall3(GL3 gl) {
    String name="right_wall";
    Material material = new Material(new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    // right wall
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(size*0.5f,size*0.5f,0), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices2.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
    ModelMultipleLights model = new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, t2);
    return model;
  }

  private ModelMultipleLights makeWall4(GL3 gl) {
    String name="roof";
    Material material = new Material(new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    // roof
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(180), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0,size*1f,0), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
    ModelMultipleLights model = new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, t4);
    return model;
  }

  private ModelMultipleLights makeWall5(GL3 gl) {
    String name="starbox_floor";
    Vec3 basecolor = new Vec3(0.5f, 0.5f, 0.5f); // grey
    Material material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    // create starbox_floor
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-size*1f,0,0), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_moving.txt", "assets/shaders/fs_standard_moving_1t.txt");
    ModelMultipleLights model = new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, t5);
    return model;
  }

  private ModelMultipleLights makeWall6(GL3 gl) {
    String name="starboxback_wall";
    Vec3 basecolor = new Vec3(0.5f, 0.5f, 0.5f); // grey
    Material material = new Material(new Vec3(0.1f, 0.1f, 0.1f), new Vec3(0.7f, 0.7f, 0.7f), new Vec3(1.0f, 1.0f, 1.0f), 32.0f);
    // starboxback_wall
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-size*1f,size*0.5f,-size*0.5f), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_moving.txt", "assets/shaders/fs_standard_moving_1t.txt");
    ModelMultipleLights model = new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, t5);
    return model;
  }

  private ModelMultipleLights makeWall7(GL3 gl) {
    String name="starboxleft_wall";
    Material material = new Material(new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    // starboxleft_wall
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-size*1.5f,size*0.5f,0), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_moving.txt", "assets/shaders/fs_standard_moving_1t.txt");
    ModelMultipleLights model = new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, t5);
    return model;
  }

  private ModelMultipleLights makeWall8(GL3 gl) {
    String name="starboxback_wall";
    Vec3 basecolor = new Vec3(0.5f, 0.5f, 0.5f); // grey
    Material material = new Material(new Vec3(0.1f, 0.1f, 0.1f), new Vec3(0.7f, 0.7f, 0.7f), new Vec3(1.0f, 1.0f, 1.0f), 32.0f);
    // starboxback_wall
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-size*1f,size*0.5f,size*0.5f), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_moving.txt", "assets/shaders/fs_standard_moving_1t.txt");
    ModelMultipleLights model = new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, t5);
    return model;
  }

  private ModelMultipleLights makeWall9(GL3 gl) {
    String name="starbox_roof";
    Material material = new Material(new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    // starbox_roof
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(180), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-size*1f,size*1f,0), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_moving.txt", "assets/shaders/fs_standard_moving_1t.txt");
    ModelMultipleLights model = new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, t5);
    return model;
  }

  public void render(GL3 gl) {
    for (int i=0; i<10; i++) {
       wall[i].render(gl);
    }
  }

  public void dispose(GL3 gl) {
    for (int i=0; i<10; i++) {
      wall[i].dispose(gl);
    }
  }
}