import gmaths.*;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;

public class Model {
  
  private String name;
  private Mesh mesh;
  private Mat4 modelMatrix;
  private Shader shader;
  private Material material;
  private Camera camera;
  private Light light;
  
  public Model() {
    name = null;
    mesh = null;
    modelMatrix = null;
    material = null;
    camera = null;
    light = null;
    shader = null;
  }
  
  public Model(String name, Mesh mesh, Mat4 modelMatrix, Shader shader, Material material, Light light, Camera camera) {
    this.name = name;
    this.mesh = mesh;
    this.modelMatrix = modelMatrix;
    this.shader = shader;
    this.material = material;
    this.light = light;
    this.camera = camera;
  }
  
  public void setName(String s) {
    this.name = s;
  }

  public void setMesh(Mesh m) {
    this.mesh = m;
  }

  public void setModelMatrix(Mat4 m) {
    modelMatrix = m;
  }
  
  public void setMaterial(Material material) {
    this.material = material;
  }

  public void setShader(Shader shader) {
    this.shader = shader;
  }

  public void setCamera(Camera camera) {
    this.camera = camera;
  }
  
  public void setLight(Light light) {
    this.light = light;
  }

  public void renderName(GL3 gl) {
    System.out.println("Name = "+name);  
  }

  public void render(GL3 gl) {
    render(gl, modelMatrix);
  }

  // second version of render is so that modelMatrix can be overriden with a new parameter
  public void render(GL3 gl, Mat4 modelMatrix) {
    if (anything_null()) {
      System.out.println("Error: null in model render");
      return;
    }

    Mat4 mvpMatrix = Mat4.multiply(camera.getPerspectiveMatrix(), Mat4.multiply(camera.getViewMatrix(), modelMatrix));

    // set shader variables. Be careful that these variables exist in the shader

    shader.use(gl);

    shader.setFloatArray(gl, "model", modelMatrix.toFloatArrayForGLSL());
    shader.setFloatArray(gl, "mvpMatrix", mvpMatrix.toFloatArrayForGLSL());
    
    shader.setVec3(gl, "viewPos", camera.getPosition());

    shader.setVec3(gl, "light.position", light.getPosition());
    shader.setVec3(gl, "light.ambient", light.getMaterial().getAmbient());
    shader.setVec3(gl, "light.diffuse", light.getMaterial().getDiffuse());
    shader.setVec3(gl, "light.specular", light.getMaterial().getSpecular());

    shader.setVec3(gl, "material.ambient", material.getAmbient());
    shader.setVec3(gl, "material.diffuse", material.getDiffuse());
    shader.setVec3(gl, "material.specular", material.getSpecular());
    shader.setFloat(gl, "material.shininess", material.getShininess());

    // then render the mesh
    mesh.render(gl);
  } 
  
  private boolean anything_null() {
    return (mesh==null);
  }

  public void dispose(GL3 gl) {
    mesh.dispose(gl);  // only need to dispose of mesh
  }
  
}