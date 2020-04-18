package law_simulation;

import java.util.List;

import br.com.agentdevlaw.legislation.Law;
import br.com.agentdevlaw.middleware.QueryProcess;
import br.com.agentdevlaw.ontology.OntologyConfigurator;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class Government extends Agent{
	
	protected void setup() {
		
		System.out.println("Government Agent is ready!");
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		
		addBehaviour(new TickerBehaviour(this, 6000) {
			protected void onTick() {
				
				OntologyConfigurator ontology = new OntologyConfigurator();
				ontology.setOrigin(OntologyConfigurator.MODEL);
				QueryProcess middleware = new QueryProcess(ontology);
				List<Law> laws =  middleware.searchAction("fish", "fisherman");
				for(int i = 0; i < laws.size(); i++) {
					System.out.println("Law -> " + laws.get(i).getIndividual());
					for(int j = 0; j < laws.get(i).getNorms().size(); j++) {
						System.out.println("Norm ->" + laws.get(i).getNorms().get(j).getIndividual());
						System.out.print("Consequence -> " + laws.get(i).getNorms().get(j).getConsequence());
						System.out.println(" (" + laws.get(i).getNorms().get(j).getConsequenceType() + ")");
					}

				}
				if(laws.isEmpty()) {//if not exists a legislation against the
					//executed action, register the river resource
					//in yellow pages
					
					dfd.setName(getAID());
					sd.setType("river");
					sd.setName("River access permission");
					dfd.addServices(sd);
					try {
						DFAgentDescription[] result  = DFService.search(myAgent, dfd);
						//if result.length == 0 the service is not registered
						if(result.length <= 0) DFService.register(myAgent, dfd);
					}
					catch (FIPAException fe) {
						fe.printStackTrace();
					}				
					
				}else {
					
					dfd.removeServices(sd);
					System.out.println("Fishing is not allowed");
					
				}
			}
		});
	}
	
	protected void takeDown() {
		try {
			DFService.deregister(this);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println("Government agent is out");
	}

}
