public final class TwoTriangles {
  
  // ***************************************************
  /* THE DATA
   */
  // anticlockwise/counterclockwise ordering
  public static final float[] vertices = {      // position, colour, tex coords
    -0.5f, 0.0f, -0.5f,  0.0f, 1.0f, 0.0f,  0.0f, 1.0f,  // top left
    -0.5f, 0.0f,  0.5f,  0.0f, 1.0f, 0.0f,  0.0f, 0.0f,  // bottom left
     0.5f, 0.0f,  0.5f,  0.0f, 1.0f, 0.0f,  1.0f, 0.0f,  // bottom right
     0.5f, 0.0f, -0.5f,  0.0f, 1.0f, 0.0f,  1.0f, 1.0f   // top right
  };

  public static final float[] vertices2 = {      // position, colour, tex coords
    -0.5f, 0.0f, -0.5f,  0.0f, 1.0f, 0.0f,  0.0f, 4.0f,  // top left
    -0.5f, 0.0f,  0.5f,  0.0f, 1.0f, 0.0f,  0.0f, 0.0f,  // bottom left
     0.5f, 0.0f,  0.5f,  0.0f, 1.0f, 0.0f,  4.0f, 0.0f,  // bottom right
     0.5f, 0.0f, -0.5f,  0.0f, 1.0f, 0.0f,  4.0f, 4.0f   // top right
  };

  public static final float[] vertices3 = {      // position, colour, tex coords
    -0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, // Top-left 
    0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, // Top-right 
    -0.5f, 0.2f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.4f, // Bottom-left 
    0.5f, 0.2f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.4f, // Bottom-right
    
    // Bottom rectangle (spans full width below the hole) 
    -0.5f, -0.2f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.2f, // Top-left 
    0.5f, -0.2f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.2f, // Top-right 
    -0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, // Bottom-left 
    0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, // Bottom-right

    // Left rectangle (left side of the hole) 
    -0.5f, 0.2f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.4f, // Top-left 
    -0.2f, 0.2f, 0.0f, 1.0f, 1.0f, 1.0f, 0.2f, 0.4f, // Top-right 
    -0.5f, -0.2f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.2f, // Bottom-left 
    -0.2f, -0.2f, 0.0f, 1.0f, 1.0f, 1.0f, 0.2f, 0.2f, // Bottom-right 

    // Right rectangle (right side of the hole) 
    0.2f, 0.2f, 0.0f, 1.0f, 1.0f, 1.0f, 0.4f, 0.4f, // Top-left 
    0.5f, 0.2f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.4f, // Top-right 
    0.2f, -0.2f, 0.0f, 1.0f, 1.0f, 1.0f, 0.4f, 0.2f, // Bottom-left 
    0.5f, -0.2f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.2f, // Bottom-right
  };

  public static final int[] indices = {         // Note that we start from 0!
      0, 1, 2,
      0, 2, 3
  };

  public static final int[] indices2 = {         // Note that we start from 0!
    // Top rectangle
    0, 1, 2,  1, 3, 2,

    // Bottom rectangle
    4, 5, 6,  5, 7, 6,

    // Left rectangle
    8, 9, 10,  9, 11, 10,

    // Right rectangle
    12, 13, 14,  13, 15, 14
  };

}