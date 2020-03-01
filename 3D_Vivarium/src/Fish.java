/*
 * Fish.java
 *
 * Xiaoxin Gan
 *
 * for the model fish, eat food and eaten by predator.
 */

import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.jogamp.opengl.util.*;

import com.jogamp.opengl.util.gl2.GLUT;

import java.util.*;

public class Fish extends Component implements Animate{

	private Vivarium vivarium;
	private int fish_CallList;
	//for the part of fish
	private int body, tail, lowfin;
	
	private float scale;
	// rotation angle
	private float rotateAngle;
	// change of rotation angle
	private float PredatorRotate;
	//current position
	public float x,y,z;
	  
	//previous position of the model
	public float prev_x,prev_y,prev_z;
	  
	//velocity of xyz 
	private float vx,vy,vz;
	// direction of movment
	private float dirx,diry,dirz;
 	private Random rand = new Random();
  	public boolean dead;
  	
  	//weight for the potential function
    private float Predator_Potential = 0.5f;
    private float Wall_Potential = 0.1f;
    private float Food_Potential = -0.6f;
    //for get information of other java function
    private Predator predator = null;
    private Food food = null;
	    

	public Fish(Point3D p, float scale_,Vivarium vivarium_) {
		super(new Point3D(p));
		
		scale = scale_;
	   
	    prev_x = x = (float) p.x();
	    prev_y = y = (float) p.y();
	    prev_z = z = (float) p.z();
	    
	    vx = 0.003f;
	    vy = 0.003f;
	    vz = 0.003f;
	    // set start direction
	    dirx = 1;
	    diry = 1;
	    dirz = 1;
	    //check dead
	    dead = false;
	    
	  //set angle
	    rotateAngle = 0;
	    PredatorRotate = 1;
	    
	    vivarium = vivarium_;
	  }

	  public void initialize( GL2 gl )
	  {
	      // draw all parts of the predator
	      Fish_body(gl);
	      Fish_tail(gl);
	      Fish_lowfin(gl);

	    fish_CallList = gl.glGenLists(1);
	    gl.glNewList( fish_CallList, GL2.GL_COMPILE );
	    GLUT glut = new GLUT();
	     // update the location and orientation , face in direction they are moving
	    Fish_display(gl);
	    gl.glEndList();
	  }

	    //draw body of fishes
	    private void Fish_body(GL2 gl){
	        body = gl.glGenLists(1);
	        gl.glNewList(body, GL2.GL_COMPILE );
	        GLUT glut = new GLUT();
	        gl.glPushMatrix();
	        gl.glScalef(2.0f, 1.0f, 1.0f);
	        glut.glutSolidSphere(0.1,36,28);
	        gl.glPopMatrix();
	        gl.glEndList();
	    }
	    //draw tail of fishes
	    private void Fish_tail(GL2 gl){
	        tail = gl.glGenLists(1);
	        gl.glNewList(tail, GL2.GL_COMPILE );
	        GLUT glut = new GLUT();
	        gl.glPushMatrix();
	        gl.glTranslated(.25f, 0f, 0f);
	        gl.glScaled(1,1.5,0.3);
	        glut.glutSolidSphere(0.1,36,28);
	        gl.glPopMatrix();
	        gl.glEndList();
	    }
	    private void Fish_lowfin(GL2 gl){
	        lowfin = gl.glGenLists(1);
	        gl.glNewList(lowfin, GL2.GL_COMPILE );
	        GLUT glut = new GLUT();
	        gl.glPushMatrix();
	        gl.glTranslated(0f, -0.07f, 0f);
	        gl.glScaled(1.1,1,10);
	        glut.glutSolidSphere(0.03,36,28);
	        gl.glPopMatrix();
	        gl.glEndList();
	    }
	    //draw the model and make the it face to the direction it moves by get the transformed matrix
	    private void Fish_display( GL2 gl) {
	        gl.glPushMatrix();
	        gl.glScalef(scale, scale, scale);

	     // get the distance for xyz in last frame, and calculate the velocity on x y z axis
	        float dx = 0-prev_x;
	        float dy = 0-prev_y;
	        float dz = 0-prev_z;

	        // normalized
	        float mag = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
	        // get velocity for xyz
	        float[] v = new float[3];
	        v[0] = dx / mag;
	        v[1] = dy / mag;
	        v[2] = dz / mag;

	        // build new uvn system
	        // assume u vector is 0,1,0
	        float[] u = { 0.0f, 1.0f, 0.0f };

	        // cross product v vector and u vector get n vector
	        float[] n = {v[1] * u[2] - u[1] * v[2], v[2] * u[0] - v[0] * u[2], v[0] * u[1] - v[1] * u[0]};

	        // use v, n, u vector and x,y,z to make new transforming matrix
	        float[] rotationMatrix = { v[0], v[1], v[2], 0.0f, u[0], u[1], u[2], 0.0f, n[0], n[1], n[2], 0.0f, x, y, z, 1.0f };


	        // use current matrix to multiply the new transforming matrix to change the model's location and face to direction
	        gl.glMultMatrixf(rotationMatrix, 0);

	        // use new transforming matrix for each part for each part of the body
	        // set fish's color
	        gl.glColor3f(0,1,1); 
	        //body
	        gl.glPushMatrix();
	        gl.glCallList(body);
	        gl.glPopMatrix();
	        
	        //lowfin
	        gl.glPushMatrix();
	        gl.glCallList(lowfin);
	        gl.glPopMatrix();
	        

	        //tail
	        gl.glPushMatrix();
	       // animation for tail
	        rotateAngle += PredatorRotate;
	        if (rotateAngle > 20 || rotateAngle < -20) {
	            PredatorRotate *= -1;
	        }
	        gl.glRotated(rotateAngle,0,1,0);
	        gl.glCallList(tail);
	        gl.glPopMatrix();

	        gl.glPopMatrix();
	    }
	  

	  public void draw(GL2 gl)
	  {
	      gl.glPushMatrix();
	      gl.glCallList( fish_CallList );
	      gl.glPopMatrix();
	  }

	    // Gaussian Potential f(p,q) = 2 *(p-q)*e^(-(p-q)T(p-q))
	  	// weight for limit the speed
	    private Point3D potentialFunction(Point3D p, Point3D q1, float weight) {
	        float x = (float) (2*weight*(p.x() - q1.x())*Math.pow(Math.E,-1*(Math.pow((p.x()-q1.x()), 2) + Math.pow((p.y()-q1.y()), 2) + Math.pow((p.z()-q1.z()), 2)) ));
	        float y = (float) (2*weight*(p.y() - q1.y())*Math.pow(Math.E,-1*(Math.pow((p.x()-q1.x()), 2) + Math.pow((p.y()-q1.y()), 2) + Math.pow((p.z()-q1.z()), 2)) ));
	        float z = (float) (2*weight*(p.z() - q1.z())*Math.pow(Math.E,-1*(Math.pow((p.x()-q1.x()), 2) + Math.pow((p.y()-q1.y()), 2) + Math.pow((p.z()-q1.z()), 2)) ));
	        Point3D potential = new Point3D(x, y, z);
	        return potential;
	    }

	    // for many creature, sum up all, ftotal(p,q1,q2,q3,q4,q5,q6,q7) = SUM fi(p,qi)
	    // add the wall so that creature will get a stronger repulsion to move away from wall avoid stuck
	    private void calcPotential() {
	    	// create the fish position
	        Point3D p = new Point3D(x,y,z);
	        
	        // check the predator position
	        
	        Point3D q1 = new Point3D(predator.x, predator.y, predator.z);
	        
	        // get the position of the wall 
	        Point3D q2 = new Point3D(1.9, y, z);
	        Point3D q3 = new Point3D(-1.9, y, z);
	        Point3D q4 = new Point3D(x, 1.9, z);
	        Point3D q5 = new Point3D(x, -1.9, z);
	        Point3D q6 = new Point3D(x, y, 1.9);
	        Point3D q7 = new Point3D(x, y, -1.9);

	        // calculate and sum all the potential function of q1,q2,q3,q4,q5,q6,q7
	        Point3D[] coords = {potentialFunction(p,q1,Predator_Potential), potentialFunction(p,q2, Wall_Potential),
	        		potentialFunction(p,q3,Wall_Potential), potentialFunction(p,q4,Wall_Potential),
	        		potentialFunction(p,q5,Wall_Potential), potentialFunction(p,q6,Wall_Potential),
	        		potentialFunction(p,q7,Wall_Potential)};
	        Point3D potential = new Point3D(0,0,0);
	        double newx = potential.x();
	        double newy = potential.y();
	        double newz = potential.z();
	        for(int i = 0; i < coords.length; i++)
	        {
	        	newx  += coords[i].x();
	        	newy  += coords[i].y();
	        	newz  += coords[i].z();
	        }

	        // calculate and sum all the potential function of foods
	        for (Food f : vivarium.foods) {
	            Point3D qi = new Point3D(f.x, f.y, f.z);
	            qi = potentialFunction(p, qi, Food_Potential);
	            newx += qi.x();
	            newy += qi.y();
	            newz += qi.z();
	        }

	        // update the new dirx, diry, dirz
	        dirx += newx;
	        diry +=  newy;
	        dirz +=  newz;
	    }

	    // get the predator information for fishes
	    public void getpredator(Predator p) {
	        predator = p;
	    }

	    // get the food information for fishes
	    public void getfood(Food f) {
	        food = f;
	    }
	
    
   
	    // if creature hit the wall it will reflect back 
	    public void hit_wall() {
	    	//make a large enough offset to compensate for the model
	    	  float nx = rand.nextFloat();
	          while (nx < 0.2f) {
	              nx = rand.nextFloat(); }
	          float ny = rand.nextFloat();
	          while (ny < 0.2f) {
	              ny = rand.nextFloat(); }
	          float nz = rand.nextFloat();
	          while (nz < 0.2f) {
	              nz = rand.nextFloat(); }
	          if (x > 1.9 || x < -1.9) {
	              if(x > 1.9){
	                  x = 1.9f;
	                  dirx = -nx;
	              }
	              else {
	                  x = -1.9f;
	                  dirx = nx;
	              }
	          }
	          if (y > 1.9 || y < -1.9) {
	              if(y > 1.9){
	                  y = 1.9f;
	                  diry = -nx;
	              }
	              else {
	                  y = -1.9f;
	                  diry = nx;
	              }
	          }
	          if (z > 1.9 || z < -1.9) {
	              if(z > 1.9){
	                  z = 1.9f;
	                  dirz = -nx;
	              }
	              else {
	                  z = -1.9f;
	                  dirz = nx;
	              }
	          }
	    	
	    	
	    }
    @Override
	public void setModelStates(ArrayList<Configuration> config_list) {
		if (config_list.size() > 1) {
			this.setConfiguration(config_list.get(0));
		}
	}
	
	
    public void animationUpdate(GL2 gl)
	  {
	      gl.glNewList(fish_CallList, GL2.GL_COMPILE );
	      // calculate move distance in last frame
	      prev_x = dirx;
	      prev_y = diry;
	      prev_z = dirz;
	      // calculate potential function and output new dirx, diry, dirz
	      calcPotential();

	      // do wall collision and reflect after touch the walls
	      hit_wall();
	   
	      // detect if predator eat the fish and the fish die
	      if (Math.sqrt(Math.pow((x-predator.x),2)+Math.pow((y-predator.y),2)+Math.pow((z-predator.z),2)) < .5f) {
	          //set dead color

	          // if dead set the dead flag and delete the prey object for predator
	     
	    	  predator.deletefish();
	    	  
	          dead = true;
	      
	      }
	      // detect if fish eat the food and the food is eaten
	      for (Food f : vivarium.foods) {
	          if (Math.sqrt(Math.pow((x-f.x),2)+Math.pow((y-f.y),2)+Math.pow((z-f.z),2)) < 0.2) {
	              f.eat = true;
	          }
	      }

	      //calculate new x,y,z
	      x+=vx*dirx;
	      y+=vy*diry;
	      z+=vz*dirz;
	      // draw the new model in new location and direction
	      Fish_display( gl );
	      gl.glEndList();

	  }

}

