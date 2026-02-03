package otros;

class PoolData {
	private String pool;
	private Double totalFees;
	private Double totalIncentives;
	private Double totalVotes;

	public PoolData(String pool, Double totalFees, Double totalIncentives, Double totalVotes) {
		this.pool = pool;
		this.totalFees = totalFees;
		this.totalIncentives = totalIncentives;
		this.totalVotes = totalVotes;
	}

	public String getPool() {
		return pool;
	}

	public Double getTotalFees() {
		return totalFees;
	}

	public Double getTotalIncentives() {
		return totalIncentives;
	}

	public Double getTotalVotes() {
		return totalVotes;
	}
}