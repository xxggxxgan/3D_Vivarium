import java.util.Random;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.jogamp.opengl.util.gl2.GLUT;

public class FoodDisplay implements Displayable{

	
	private int callListHandle;
//	private float scale;
	private GLUquadric qd;
	public static final int DEFAULT_STACKS = 28;
	public static final int DEFAULT_SLICES = 36;
	public float x, y, z;
	private Random rand;
	public FoodDisplay() {
	//	this.scale = scale;
//		this.x = x_;
//		this.y = y_;
//		this.z = z_;
	}
	
//float x_,float y_, float z_	
	/*
	 * Method to be called for data retrieving
	 * 
	 * */
	@Override
	public void draw(GL2 gl) {
//		gl.glPushMatrix();
//		gl.glPushAttrib( GL2.GL_CURRENT_BIT );
//		gl.glTranslatef(x, y, z);
//		
//		gl.glColor3f( 0f, 1.0f, 0f);
		gl.glCallList(this.callListHandle);
//		gl.glPopAttrib();
//		gl.glPopMatrix();
	}

	/*
	 * Initialize our example model and store it in display list
	 * 
	 * */
	@Override
	public void initialize(GL2 gl) {

		this.callListHandle = gl.glGenLists(1);
		gl.glNewList(this.callListHandle, GL2.GL_COMPILE);
		
		GLU glu = new GLU();
		this.qd = glu.gluNewQuadric();
		GLUT glut = new GLUT();
		gl.glPushMatrix();
//		gl.glTranslatef(x, y, z);
		glut.glutSolidSphere(0.05f, 25, 18);
		gl.glPopMatrix();
		gl.glEndList();
		
	}

}
