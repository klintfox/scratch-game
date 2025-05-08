package com.klinux.scratch.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WinCombination {

	@JsonProperty("reward_multiplier")
	private double rewardMultiplier;

	private String when;
	private Integer count;
	private String group;

	@JsonProperty("covered_areas")
	private List<List<String>> coveredAreas;

	// Constructor que acepta estos cuatro parámetros
	@JsonCreator
	public WinCombination(@JsonProperty("reward_multiplier") double rewardMultiplier, @JsonProperty("when") String when,
			@JsonProperty("count") int count, @JsonProperty("group") String group) {
		this.rewardMultiplier = rewardMultiplier;
		this.when = when;
		this.count = count;
		this.group = group;
	}

	public double getRewardMultiplier() {
		return rewardMultiplier;
	}

	public String getWhen() {
		return when;
	}

	public Integer getCount() {
		return count;
	}

	public String getGroup() {
		return group;
	}

	public List<List<String>> getCoveredAreas() {
		return coveredAreas;
	}

	public void setRewardMultiplier(double rewardMultiplier) {
		this.rewardMultiplier = rewardMultiplier;
	}

	public void setWhen(String when) {
		this.when = when;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public void setCoveredAreas(List<List<String>> coveredAreas) {
		this.coveredAreas = coveredAreas;
	}

	@Override
	public String toString() {
		return "WinCombination [rewardMultiplier=" + rewardMultiplier + ", when=" + when + ", count=" + count
				+ ", group=" + group + ", coveredAreas=" + coveredAreas + "]";
	}

}
