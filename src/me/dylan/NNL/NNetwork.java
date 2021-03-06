package me.dylan.NNL;

import java.util.ArrayList;

import me.dylan.NNL.NNLib.NodeType;
import me.dylan.NNL.Utils.StringUtil;

/**
 * A wrapper around an entire neural network.
 * 
 */
public class NNetwork {
	private ArrayList<HiddenNode> networkHidden = new ArrayList<HiddenNode>();
	private ArrayList<Synapse> networkSynapses = new ArrayList<Synapse>();
	private ArrayList<Input> networkInputs = new ArrayList<Input>();
	private ArrayList<Output> networkOutputs = new ArrayList<Output>();
	private boolean isLearningMode = false;
	String desiredOutput = "";

	/**
	 * Takes in the required values and setups the initial network based on the
	 * values received
	 * 
	 * @param inputs
	 *            The input nodes that need to be added to this network
	 * @param connections
	 *            The synapse connections that need to be added to this network
	 * @param outputs
	 *            The output nodes that need to be added to this network
	 * @param desiredOutput
	 *            The desired output that the network should be striving for
	 *            during training
	 */
	public NNetwork(ArrayList<Input> inputs, ArrayList<HiddenNode> connections,
			ArrayList<Output> outputs, String desiredOutput) {
		this.networkHidden = connections;
		this.networkInputs = inputs;
		this.networkOutputs = outputs;
		this.desiredOutput = desiredOutput;
	}

	@Deprecated
	// Deprecated on 3/16
	public NNetwork(String desiredOutput) {
		this.desiredOutput = desiredOutput;
	}

	// TODO: Combine into addManyNodesToNetwork
	public void addOutputNodeToNetwork(Output out) {
		networkOutputs.add(out);
	}

	// TODO: Combine into addManyNodesToNetwork
	public void addInputNodeToNetwork(Input in) {
		networkInputs.add(in);
	}

	// TODO: Combine into addManyNodesToNetwork
	public void addManyInputNodesToNetwork(ArrayList<Input> inputs) {
		for (Input input : inputs) {
			addInputNodeToNetwork(input);
		}
	}

	// TODO: Combine into addManyNodesToNetwork
	public void addManyOutputNodesToNetwork(ArrayList<Output> outputs) {
		for (Output output : outputs) {
			addOutputNodeToNetwork(output);
		}
	}

	/**
	 * Add the passed hidden node to the list of all hidden nodes in the network
	 * 
	 * @param Hidden
	 *            The node that needs to be added to the network
	 */
	public void addHiddenNodeToNetwork(HiddenNode Hidden) {
		networkHidden.add(Hidden);
	}

	/**
	 * Takes a list of hidden nodes, and a network, and then adds all the hidden
	 * nodes to the network
	 * 
	 * @param HiddenNodes
	 *            The list of nodes that need to be added to the network
	 */
	// TODO: Combine into addManyNodesToNetwork
	public void addManyHiddenNodesToNetwork(ArrayList<HiddenNode> HiddenNodes) {
		for (HiddenNode Hidden : HiddenNodes) {
			addHiddenNodeToNetwork(Hidden);
		}
	}

	/**
	 * Creates a list of all the input, hidden, and output nodes that currently
	 * exist in the network
	 * 
	 * @return The arraylist that contains every node that exists in the network
	 */
	// TODO: should we also be collecting the synapses and which nodes those
	// synapses belong? Why would we ever need to know which nodes exist,
	// without knowing the connections as well
	public ArrayList<Node> getNodesInNetwork() {
		ArrayList<Node> nodes = new ArrayList<Node>();
		nodes.addAll(networkHidden);
		nodes.addAll(networkInputs);
		nodes.addAll(networkOutputs);
		return nodes;
	}

	/**
	 * Returns a list of all the hidden nodes that exist within the network
	 * 
	 * @return an arraylist of all hidden nodes
	 */
	public ArrayList<HiddenNode> getHiddenNodesInNetwork() {
		return networkHidden;
	}

	/**
	 * Returns a list of all the input nodes that exist within the network
	 * 
	 * @return an arraylist of all input nodes
	 */
	public ArrayList<Input> getInputNodesInNetwork() {
		return networkInputs;
	}

	/**
	 * Returns a list of all the output nodes that exist within the network
	 * 
	 * @return an arraylist of all the output nodes
	 */
	public ArrayList<Output> getOutputNodesInNetwork() {
		return networkOutputs;
	}


	/**
	 * Finds all of the input, hidden, and output nodes and then connects them
	 */
	// TODO: Dylan: Clean up -- "Looks Terrible" - said Dylan
	public void connectAll() {
		for (Node node : getNodesInNetwork()) {
			for (HiddenNode node2 : getHiddenNodesInNetwork()) {
				boolean allowProgression = !(node.getNodeVariety() == NodeType.INPUT || node2
						.getNodeVariety() == NodeType.INPUT)
						&& !(node.getNodeVariety() == NodeType.OUTPUT || node2
								.getNodeVariety() == NodeType.OUTPUT)
						&& (node.getNodeVariety() != node2.getNodeVariety() || node
								.getNodeVariety() == NodeType.HIDDEN)
						&& !node.equals(node2);

				if (allowProgression)
					node.connectNodeToNode(node2,
							NNLib.MAX_CONNECTION_WEIGHT / 2, this);
			}
			for (Input inNode : getInputNodesInNetwork()) {
				if (inNode.getNodeVariety() != node.getNodeVariety()
						&& node.getNodeVariety() != NodeType.OUTPUT) {

					inNode.connectNodeToNode(node,
							NNLib.MAX_CONNECTION_WEIGHT / 2, this);
					if (node.getNodeVariety() == NodeType.INPUT)
						System.out.println();
				}

			}
		}

	}

	/**
	 * Takes the value of the output nodes in the network and assigns them to
	 * the String results
	 * 
	 * @return the collection of the data contained in the output nodes
	 */
	public String getNetworkOutput() {
		String result = "";
		for (Output out : getOutputNodesInNetwork()) {
			String outPulse = out.getOutputValue().getData();
			if (!outPulse.isEmpty())
				result += "|||||" + outPulse;
		}
		return result;
	}

	/**
	 * Tests the networks current output against the desired output For training
	 * purposes only
	 * 
	 * @return The percentage of how close the network is to a match
	 */
	public double getNetworkSimilarityPercentage() {
		String netOut = getNetworkOutput();
		if (netOut.isEmpty())
			return 0;
		double percentMatch = StringUtil.calculateStringSimilarityPercentage(
				netOut, desiredOutput);
		return percentMatch * 100;
	}

	/**
	 * Takes the list of synapses that exist in the network and returns it
	 * 
	 * @return the list of synapses that exist in the network
	 */
	@Deprecated
	//Deprecated on 3/29
	public ArrayList<Synapse> getNetworkSynapses() {
		return networkSynapses;
	}

	/**
	 * Creates a copy of the network synapses and then removes any synapses that
	 * contain the value null, or are connected to a node with a null value
	 */
	@Deprecated
	//Deprecated on 3/29
	public void removeUnusedSynapses() {
		ArrayList<Synapse> netSynapsesClone = (ArrayList<Synapse>) getNetworkSynapses()
				.clone();
		for (Synapse synapse : netSynapsesClone) {
			if (synapse == null) {
				removeSynapse(synapse);
				continue;
			}
			if (synapse.getConnectionDestination() == null
					|| synapse.getConnectionOrigin() == null) {
				removeSynapse(synapse);
			}
		}
	}

	/**
	 * Removes all synapses from the network list of synapses
	 */
	@Deprecated
	//Deprecated on 3/29
	public void clearSynapses() {
		networkSynapses.clear();
	}

	/**
	 * Adds the passed synapse to the networks list of synapses
	 * 
	 * @param synapse
	 *            The synapse to be added to the network list
	 */
	@Deprecated
	//Deprecated on 3/29
	public void addSynapse(Synapse synapse) {
		networkSynapses.add(synapse);
	}

	/**
	 * Severs the synapses connection to any nodes, and then deletes it
	 * 
	 * @param connection
	 *            The synapse to be severed and deleted
	 */
	@Deprecated
	//Deprecated on 3/29
	public void removeSynapse(Synapse connection) {
		if (connection != null)
			connection.severConnections();
		networkSynapses.remove(connection);
	}
}
