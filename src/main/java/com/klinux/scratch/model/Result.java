package com.klinux.scratch.model;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Result {

	private String[][] matrix;
	private double reward;
	Map<String, List<String>> appliedWinningCombinations;
	List<String> appliedBonusSymbol;

	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();

	    // matrix
	    sb.append("\"matrix\": [\n");
	    for (int i = 0; i < matrix.length; i++) {
	        sb.append("    [");
	        for (int j = 0; j < matrix[i].length; j++) {
	            sb.append("\"").append(matrix[i][j]).append("\"");
	            if (j < matrix[i].length - 1) {
	                sb.append(", ");
	            }
	        }
	        sb.append("]");
	        if (i < matrix.length - 1) {
	            sb.append(",");
	        }
	        sb.append("\n");
	    }
	    sb.append("],\n");

	    // reward
	    sb.append("\"reward\": ").append(reward).append(",\n");

	    // appliedWinningCombinations
	    sb.append("\"applied_winning_combinations\": {\n");
	    if (appliedWinningCombinations != null && !appliedWinningCombinations.isEmpty()) {
	        int i = 0;
	        for (Map.Entry<String, List<String>> entry : appliedWinningCombinations.entrySet()) {
	            sb.append("        \"").append(entry.getKey()).append("\": [");
	            List<String> values = entry.getValue();
	            for (int j = 0; j < values.size(); j++) {
	                sb.append("\"").append(values.get(j)).append("\"");
	                if (j < values.size() - 1) {
	                    sb.append(", ");
	                }
	            }
	            sb.append("]");
	            if (i < appliedWinningCombinations.size() - 1) {
	                sb.append(",");
	            }
	            sb.append("\n");
	            i++;
	        }
	    }
	    sb.append("    },\n");

	    // appliedBonusSymbol
	    sb.append("\"applied_bonus_symbol\": ");
	    if (appliedBonusSymbol != null && !appliedBonusSymbol.isEmpty()) {
	        if (appliedBonusSymbol.size() == 1) {
	            sb.append("\"").append(appliedBonusSymbol.get(0)).append("\"\n");
	        } else {
	            sb.append("[");
	            for (int i = 0; i < appliedBonusSymbol.size(); i++) {
	                sb.append("\"").append(appliedBonusSymbol.get(i)).append("\"");
	                if (i < appliedBonusSymbol.size() - 1) {
	                    sb.append(", ");
	                }
	            }
	            sb.append("]\n");
	        }
	    }

	    return sb.toString();
	}


}