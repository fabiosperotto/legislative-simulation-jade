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
				
				if(laws.isEmpty()) {//se nao existir legislacao contra a acao executada
					//registrando recurso de rio nas yellow pages
					
					dfd.setName(getAID());
					
					sd.setType("river");
					sd.setName("River resource");
					dfd.addServices(sd);
					try {
						DFService.register(myAgent, dfd);
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
