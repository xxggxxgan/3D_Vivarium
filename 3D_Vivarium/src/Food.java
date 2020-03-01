/*
 * Food.java
 *
 * Xiaoxin Gan
 *
 * for the model food, eaten by fish.
 */
import com.jogamp.opengl.util.gl2.GLUT;

import javax.media.opengl.GL2;

import java.util.ArrayList;
import java.util.Random;

public class Food extends Component implements Animate {
	private Random rand;
	private float velocity; 
	public int food;
	public boolean eat;
	public float x, y, z;

	//create Fodder
	public Food(Point3D p ) {
		super(new Point3D(p));
		rand=new Random();
		//food drop velocity
		velocity=0.006f;
		//random the value for xyz
		x=rand.nextFloat()*3.5f-2;
		y=2;
		z=rand.nextFloat()*3.5f-2;
		// check if eaten
		eat=false;
	}
	
	
	// init the food 
	public void initialize(GL2 gl) {
		food=gl.glGenLists(1);
		gl.glNewList(food, GL2.GL_COMPILE);
		GLUT glut = new GLUT();
		glut.glutSolidSphere(0.07f, 25, 18);
		gl.glEndList();
	}
	

	// draw the food
	public void draw(GL2 gl) {
		gl.glPushMatrix();
	    gl.glPushAttrib( GL2.GL_CURRENT_BIT );
	    gl.glTranslatef(x, y, z);
	    gl.glColor3f( 1f, 1.0f, 0f);
	    gl.glCallList(food);
	    gl.glPopAttrib();
	    gl.glPopMatrix();
	
	}
	
	@Override
	public void setModelStates(ArrayList<Configuration> config_list) {
		// assign configurations in config_list to all Components in here
	}
	
	//update for food dropping
	public void animationUpdate(GL2 gl) {

		if (y>-1.9f){
			y=y-velocity;
		}
		else
			y = -1.9f;
	}

}
	


