package unicl;

import java.util.LinkedList;

import kr.ac.kaist.swrc.jhannanum.comm.Eojeol;
import kr.ac.kaist.swrc.jhannanum.comm.Sentence;
import kr.ac.kaist.swrc.jhannanum.hannanum.Workflow;
import kr.ac.kaist.swrc.jhannanum.hannanum.WorkflowFactory;

public class MorphemeAnalyzer {

	public String wordSplit(String dataAll, String dictionary){
		Workflow workflow = WorkflowFactory.getPredefinedWorkflow(WorkflowFactory.WORKFLOW_NOUN_EXTRACTOR);
		try {
			/* Activate the work flow in the thread mode */
			workflow.activateWorkflow(true);
			
			/* Analysis using the work flow */
			String document = dataAll;
			String dataWord = "";
			
			workflow.analyze(document);
						
			LinkedList<Sentence> resultList = workflow.getResultOfDocument(new Sentence(0, 0, false));
			for (Sentence s : resultList) {
				Eojeol[] eojeolArray = s.getEojeols();
				for (int i = 0; i < eojeolArray.length; i++) {
					if (eojeolArray[i].length > 0) {
						String[] morphemes = eojeolArray[i].getMorphemes();
						for (int j = 0; j < morphemes.length; j++) {
							if( dictionary.contains(":"+morphemes[j]+":") ){
								morphemes[j] = dictionary.substring(dictionary.indexOf(morphemes[j]) + morphemes[j].length());
								morphemes[j] = morphemes[j].substring(1,morphemes[j].indexOf('/'));
							}							
							//한줄씩 엔
							dataWord += morphemes[j] + "\n";
						}
					}
				}
			}
			
			workflow.close();

			
			return dataWord;
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		

		return null;
	}

}
