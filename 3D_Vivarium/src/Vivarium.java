
import javax.media.opengl.*;
import com.jogamp.opengl.util.*;
import java.util.*;

public class Vivarium implements Displayable ,Animate
{
	private Tank tank;
	public ArrayList<Component> vivarium = new ArrayList<Component>();
	  public List<Fish> fishes = new ArrayList<>();
	  public List<Food> foods = new ArrayList<>();
	  public List<Predator> predators = new ArrayList<>();
	  
	  private  boolean  foodadd = false;
	  private  boolean  fishadd = false;

	  public Random rand ;
	  public float rfx;
	  public float rfy;
	  public float rfz;
	  
	  
	public Vivarium() {
		
		tank = new Tank(4.0f, 4.0f, 4.0f);
		// add fish in to the vivarium
		fishes.add(new Fish(new Point3D(-1.7f,1.7f,-1.7f), 1f,this));
		fishes.add(new Fish(new Point3D(-1.7f,-1.7f,0.3f), 1f,this));
		fishes.add(new Fish(new Point3D(1f,0.7f,0.8f), 1f,this));
		fishes.add(new Fish(new Point3D(1.7f,-1.75f,-1.75f), 1f,this));
		
		// add predators in to the vivarium
	    predators.add(new Predator(new Point3D(0f,-0.9f,-0.7f),0.8f,this));
	    
	    //predator get fish information
	    predators.get(0).getfish(fishes.get(0));
	    //fish get predator information
	    fishes.get(0).getpredator(predators.get(0));

	    predators.get(0).getfish(fishes.get(1));
	    fishes.get(1).getpredator(predators.get(0));

	    predators.get(0).getfish(fishes.get(2));
	    fishes.get(2).getpredator(predators.get(0));
	    
	    predators.get(0).getfish(fishes.get(3));
	    fishes.get(3).getpredator(predators.get(0));
	}
	public void addfood()
	  {
		// add food in to the vivarium
		foods.add(new Food(new Point3D(0,0,0)));
	    foodadd = true;
	      
	  }
	  public void newFish() {
		  rand =new Random();
			rfx=rand.nextFloat()*3.5f-2;
			rfy=rand.nextFloat()*3.5f-2;
			rfz=rand.nextFloat()*3.5f-2;
		  
		  fishes.add(new Fish(new Point3D(rfx,rfy,rfz), 1f,this));
		  fishadd = true;
		  predators.get(0).getfish(fishes.get(fishes.size()-1));
		  fishes.get(fishes.size()-1).getpredator(predators.get(0));
	  }
	
	// initialize the model
	public void initialize(GL2 gl) {
		tank.initialize(gl);
		for(Fish fish: fishes)
	    {
	        fish.initialize( gl );
	    }
		for (Food food : foods) {
			food.initialize(gl);
		}
		for(Predator predator: predators)
	    {
	      predator.initialize(gl);
	    }
	}

	// update the model
	public void update(GL2 gl) {
		tank.update(gl);
		// if fish is not null and fish is eaten by predator, then delete this fish
		if(fishes.size()!=0) {
	          for(int i = 0; i<fishes.size();i++)
	              if (fishes.get(i).dead == true) {
	                  Fish item = fishes.get(i);
	                  fishes.remove(item);
	      }
		}
		// if food is not null and fish is eaten by fish, then delete this food
	      if(foods.size()!=0) {
	          for(int i = 0; i<foods.size();i++)
	          if (foods.get(i).eat == true) {
	              Food item = foods.get(i);
	              foods.remove(item);
	          }
	      }
	      if (fishadd) {
	    	  for(Fish fish: fishes)
	  	    {
	  	        fish.initialize( gl );
	  	    }
	      	fishadd = false;
	      } 
	      
	      // update the fish if there is fish exist
		  if(fishes != null) {
			    for(Fish fish: fishes)
			    {
			        fish.animationUpdate(gl);
			    }
		  }
		  
		  // update food, let fish get the food information
		  if (foodadd == true) {
	          for (Food food : foods) {
	              food.initialize(gl);
	              if(fishes.size()!=0)
	                  fishes.get(0).getfood(foods.get(0));
	          }
	          foodadd = false;
	      }

	      for(Food food: foods)
	      {
	          food.animationUpdate(gl);
	      }
	      
	      for(Predator predator: predators)
	      {
	        predator.animationUpdate(gl);
	      }
	}

	//draw all the model
	public void draw(GL2 gl) {
		tank.draw(gl);
		for (Food food : foods) {
			food.draw(gl);
		}
	    for(Predator predator: predators)
	    {
	      predator.draw(gl);
	    }
	    if(fishes != null)
	    for(Fish fish: fishes)
	    {
	      fish.draw(gl);
	    }
	}

	
	
	
	@Override
	public void setModelStates(ArrayList<Configuration> config_list) {
		// assign configurations in config_list to all Components in here
	}

	@Override
	public void animationUpdate(GL2 gl) {

		for (Food food : foods) {
			if (food instanceof Animate) {
				((Animate) food).animationUpdate(gl);
			}
		}
	}
}
