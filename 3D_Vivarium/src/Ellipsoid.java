import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.jogamp.opengl.util.gl2.GLUT;

public class Ellipsoid implements Displayable{

	private int callListHandle;
	private float scale;
	private GLUquadric qd;
	public static final int DEFAULT_STACKS = 28;
	public static final int DEFAULT_SLICES = 36;
	public Ellipsoid() {
	//	this.scale = scale;
	}
	
	/*
	 * Method to be called for data retrieving
	 * 
	 * */
	@Override
	public void draw(GL2 gl) {
		gl.glCallList(this.callListHandle);
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
		gl.glScalef(2.0f, 1.0f, 1.0f);
		glut.glutSolidSphere(0.1,DEFAULT_SLICES,DEFAULT_STACKS);
	    
		gl.glPopMatrix();

		gl.glEndList();
	}

}
