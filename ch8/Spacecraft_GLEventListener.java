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
  
public class Spacecraft_GLEventListener implements GLEventListener {
  
  private static final boolean DISPLAY_SHADERS = false;
  private Camera camera;
    
  /* The constructor is not used to initialise anything */
  public Spacecraft_GLEventListener(Camera camera) {
    this.camera = camera;
    this.camera.setPosition(new Vec3(4f,6f,15f));
    this.camera.setTarget(new Vec3(0f,5f,0f));
  }
  
  // ***************************************************
  /*
   * METHODS DEFINED BY GLEventListener
   */

  /* Initialisation */
  public void init(GLAutoDrawable drawable) {   
    GL3 gl = drawable.getGL().getGL3();
    System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); 
    gl.glClearDepth(1.0f);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glFrontFace(GL.GL_CCW);    // default is 'CCW'
    //gl.glEnable(GL.GL_CULL_FACE); // default is 'not enabled' so needs to be enabled
    //gl.glCullFace(GL.GL_BACK);   // default is 'back', assuming CCW
    initialise(gl);
    startTime = getSeconds();
  }
  
  /* Called to indicate the drawing surface has been moved and/or resized  */
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    GL3 gl = drawable.getGL().getGL3();
    gl.glViewport(x, y, width, height);
    float aspect = (float)width/(float)height;
    camera.setPerspectiveMatrix(Mat4Transform.perspective(45, aspect));
  }

  /* Draw */
  public void display(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    render(gl);
  }

  /* Clean up memory */
  public void dispose(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    dancingRobot.dispose(gl);
    spinningGlobe.dispose(gl);
    groundRobot.dispose(gl);
    room.dispose(gl);
    lights[0].dispose(gl);
    lights[1].dispose(gl);
    textures.destroy(gl);
  }

  // ***************************************************
  /* THE SCENE
   * Now define all the methods to handle the scene.
   * This will be added to in later examples.
   */

  // textures
  private TextureLibrary textures;

  private Room room;
  private DancingRobot dancingRobot;
  private SpinningGlobe spinningGlobe;
  private GroundRobot groundRobot;
  private Light[] lights = new Light[2];

  //This is my code
  private void loadTextures(GL3 gl) {
    textures = new TextureLibrary();
    textures.add(gl, "container_diffuse", "assets/textures/container2.jpg");
    textures.add(gl, "container_specular", "assets/textures/container2_specular.jpg");
    textures.add(gl, "chequerboard", "assets/textures/chequerboard.jpg");
    textures.add(gl, "cloud", "assets/textures/cloud.jpg");
    textures.add(gl, "diffuse_precious", "assets/textures/diffuse_precious.jpg");
    textures.add(gl, "specular_precious", "assets/textures/specular_precious.jpg");
    textures.add(gl, "repeating_star", "assets/textures/repeating_star.jpg");
    textures.add(gl, "ground", "assets/textures/ground.jpg");
    textures.add(gl, "wind_roof", "assets/textures/wind_roof.jpg");
    textures.add(gl, "star", "assets/textures/star.jpg");
    textures.add(gl, "dancingrobot_texture", "assets/textures/dancingrobot_texture.jpg");
    textures.add(gl, "groundrobot_texture", "assets/textures/groundrobot_texture.jpg");
    textures.add(gl, "spinning_globe", "assets/textures/spinning_globe.jpg");
    textures.add(gl, "wood", "assets/textures/wood.jpg");
  }

  // Initiaalizing various classess
  public void initialise(GL3 gl) {
    createRandomNumbers();
    loadTextures(gl);

    lights[0] = new Light(gl);
    lights[0].setCamera(camera);
    //lights[0].setPosition(new Vec3(0.0f, 5.0f, 0.0f));
    lights[1] = new Light(gl);
    lights[1].setCamera(camera);
    //lights[1].setPosition(new Vec3(0.0f, 5.0f, 0.0f));
    dancingRobot = new DancingRobot(gl, camera, lights[0], textures.get("dancingrobot_texture"));
    spinningGlobe = new SpinningGlobe(gl, camera, lights[0], textures.get("spinning_globe"), textures.get("wood"));
    groundRobot = new GroundRobot(gl, camera, lights[1], textures.get("groundrobot_texture"), textures.get("groundrobot_texture"));
    room = new Room(gl, camera, lights, textures.get("diffuse_precious"), 
    textures.get("specular_precious"), 
    textures.get("repeating_star"), 
    textures.get("ground"), 
    textures.get("wind_roof"),
    textures.get("star"));
  }
  
  public void render(GL3 gl) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

    lights[0].setPosition(getLight0Position());  // changing light position each frame
    lights[0].render(gl);

    lights[1].setPosition(getLight1Position());  // changing light position each frame
    lights[1].render(gl);
    
    groundRobot.render(gl);
    spinningGlobe.render(gl);
    dancingRobot.render(gl);
    room.render(gl);
  }
  
  // The light's position is continually being changed, so needs to be calculated for each frame.
  private Vec3 getLight0Position() {
    double elapsedTime = getSeconds()-startTime;
    float x = 8.0f*(float)(Math.sin(Math.toRadians(elapsedTime*50)));
    float y = 3.4f;
    float z = 5.0f*(float)(Math.cos(Math.toRadians(elapsedTime*50)));
    return new Vec3(x,y,z);
  }

  private Vec3 getLight1Position() {
    double elapsedTime = getSeconds()-startTime;
    float x = 8.0f*(float)(Math.sin(Math.toRadians(elapsedTime*80)));
    float y = 7.4f;
    float z = 3.0f*(float)(Math.cos(Math.toRadians(elapsedTime*80)));
    return new Vec3(x,y,z);
  }

  // This method is used to set a random position for each container 
  // and a rotation based on the elapsed time.
  private Mat4 getModelMatrix(int i) {
    double elapsedTime = getSeconds()-startTime;
    Mat4 m = new Mat4(1);    
    float yAngle = (float)(elapsedTime*10*randoms[(i+637)%NUM_RANDOMS]);
    float multiplier = 12.0f;
    float x = multiplier*randoms[i%NUM_RANDOMS] - multiplier*0.5f;
    float y = 0.5f+ (multiplier*0.5f) + multiplier*randoms[(i+137)%NUM_RANDOMS] - multiplier*0.5f;
    float z = multiplier*randoms[(i+563)%NUM_RANDOMS] - multiplier*0.5f;
    m = Mat4.multiply(m, Mat4Transform.translate(x,y,z));
    m = Mat4.multiply(m, Mat4Transform.rotateAroundY(yAngle));
    return m;
  }
  
    // ***************************************************
  /* TIME
   */ 
  
  private double startTime;
  
  private double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }
  
    // ***************************************************
  /* An array of random numbers
   */ 
  
  private int NUM_RANDOMS = 1000;
  private float[] randoms;
  
  private void createRandomNumbers() {
    randoms = new float[NUM_RANDOMS];
    for (int i=0; i<NUM_RANDOMS; ++i) {
      randoms[i] = (float)Math.random();
    }
  }
  
}