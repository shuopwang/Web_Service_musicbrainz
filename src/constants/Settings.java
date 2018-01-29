package constants;

public class Settings {

	public static final String rootProject="/Users/codeur/Desktop/M2/S2/dymanic content magement/Code/WS-Evaluation/";
	public static final String dirWithDef=rootProject+"ws-definitions/";
	
	
	public static final String getDirForCallResults(String ws){
		return rootProject+ws+"/call_results/";
	}
	
	public static final String getDirForTransformationResults(String ws){
		return rootProject+ws+"/transf_results/";
	}
	
	
	
}
