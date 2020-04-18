package law_simulation;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class Fisherman extends Agent{
	
	private String role = "fisherman";
	
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}


	protected void setup() {
		System.out.println("Agent "+this.role+" is ready.");
		addBehaviour(new TickerBehaviour(this, 10000) {
			protected void onTick() {
				System.out.println("Trying to fish");

				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("river");
				template.addServices(sd);
				try {
					DFAgentDescription[] result = DFService.search(myAgent, template); 
					
					if(result.length > 0) { //exists a river resource in yellow pages
						System.out.println("Exists river access permission!");						
						myAgent.addBehaviour(new FishPerformer());							
						
					}else {
						System.out.println("River access denied");
					}
				}
				catch (FIPAException fe) {
					fe.printStackTrace();
				}
			}
		});
	}
	
	protected void takeDown() {
		System.out.println("Fisherman "+this.role+" terminating.");
	}
	
	private class FishPerformer extends Behaviour {
		

		public void action() {
			System.out.println("Fisherman is fishing");
			
		}
		
		public boolean done(){
			System.out.println("Terminating fishing");
			return true;
		}
	}
	

}
