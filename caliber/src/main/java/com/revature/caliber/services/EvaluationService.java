package com.revature.caliber.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.caliber.beans.Grade;
import com.revature.caliber.beans.Note;
import com.revature.caliber.data.GradeDAO;
import com.revature.caliber.data.NoteDAO;

/**
 * Used to add grades for assessments and input notes Application logic has no
 * business being in a DAO nor in a Controller. This is the ideal place for
 * calculations
 * 
 * @author Patrick Walsh
 *
 */
@Service
public class EvaluationService {

	private static final Logger log = Logger.getLogger(EvaluationService.class);
	private GradeDAO gradeDAO;
	private NoteDAO noteDAO;
	private static final String FINDING_WEEK = "Finding week ";

	@Autowired
	public void setGradeDAO(GradeDAO gradeDAO) {
		this.gradeDAO = gradeDAO;
	}

	@Autowired
	public void setNoteDAO(NoteDAO noteDAO) {
		this.noteDAO = noteDAO;
	}

	/*
	 *******************************************************
	 * GRADING SERVICES
	 *
	 *******************************************************
	 */

	/**
	 * SAVE GRADE
	 * 
	 * @param grade
	 */
	public void save(Grade grade) {
		log.debug("Saving grade: " + grade);
		gradeDAO.save(grade);
	}

	/**
	 * UPDATE GRADE
	 * 
	 * @param grade
	 */
	public void update(Grade grade) {
		log.debug("Updating grade: " + grade);
		gradeDAO.update(grade);
	}


	/**
	 * FIND GRADES BY WEEK
	 * 
	 * @param batchId
	 * @param week
	 * @return
	 */
	public Map<Integer, List<Grade>> findGradesByWeek(Integer batchId, Integer week) {
		log.debug(FINDING_WEEK + week + " grades for batch: " + batchId);
		List<Grade> grades = gradeDAO.findByWeek(batchId, week);
		Map<Integer, List<Grade>> table = new HashMap<>();
		for(Grade grade : grades){
			Integer key = grade.getTrainee().getTraineeId();
			if(table.containsKey(grade.getTrainee().getTraineeId())){
				// eliminate nested records first
				grade.getAssessment().setBatch(null);
				// get the trainee's assessments and add the new assessment
				table.get(key).add(grade);
			}else{
				// eliminate nested records first
				grade.getAssessment().setBatch(null);
				// add the first assessment
				List<Grade> assessments = new ArrayList<>();
				assessments.add(grade);
				table.put(key, assessments);
			}
		}
		return table;
	}

	/*
	 *******************************************************
	 * NOTE SERVICES
	 *
	 *******************************************************
	 */

	/**
	 * SAVE NOTE
	 * 
	 * @param note
	 */
	public int save(Note note) {
		log.debug("Saving note: " + note);
		return noteDAO.save(note);
	}

	/**
	 * UPDATE NOTE
	 * 
	 * @param note
	 */
	public void update(Note note) {
		log.debug("Updating note: " + note);
		noteDAO.update(note);
	}

	/**
	 * FIND WEEKLY BATCH NOTES (TRAINER/PUBLIC)
	 * 
	 * @param batch
	 * @param week
	 * @return
	 */
	public List<Note> findBatchNotes(Integer batchId, Integer week) {
		log.debug(FINDING_WEEK + week + " batch notes for batch: " + batchId);
		return noteDAO.findBatchNotes(batchId, week);
	}

	/**
	 * FIND WEEKLY INDIVIDUAL NOTES (TRAINER/PUBLIC)
	 * 
	 * @param trainee
	 * @param week
	 * @return
	 */
	public List<Note> findIndividualNotes(Integer batchId, Integer week) {
		log.debug(FINDING_WEEK + week + " individual notes for batch: " + batchId);
		List <Note> notesTemp = noteDAO.findIndividualNotes(batchId,week);
		List <Note> notes = new ArrayList<>();
		if(notesTemp !=null){
			for(Note n : notesTemp){
				n.setBatch(null);
				notes.add(n);
			}
		}
		return notes;
	}
	
	/**
	 * FIND TRAINEE NOTE FOR THE WEEK 
	 * 
	 * @param trainee
	 * @param week
	 * @return 
	 */
	public Note findTraineeNote(Integer traineeId, Integer week) {
		Note note = noteDAO.findTraineeNote(traineeId,week);
		
		note.setBatch(null);
		
		return note;
	}
	
	
	/**
	 * FIND QCTRAINEE NOTE FOR THE WEEK(Michael) 
	 * 
	 * @param trainee
	 * @param week
	 * @return 
	 */
	public Note findQCTraineeNote(Integer traineeId, Integer week) {
		Note note = noteDAO.findQCTraineeNote(traineeId,week);
		
		note.setBatch(null);
		
		return note;
	}

	/**
	 * FIND WEEKLY QC BATCH NOTES (NOT FOR TRAINERS)
	 * 
	 * @param batch
	 * @param week
	 * @return
	 */
	public Note findQCBatchNotes(Integer batchId, Integer week) {
		log.debug(FINDING_WEEK + week + " QC batch notes for batch: " + batchId);
		return noteDAO.findQCBatchNotes(batchId, week);
	}

	/**
	 * FIND WEEKLY QC INDIVIDUAL NOTES (NOT FOR TRAINERS)
	 * 
	 * @param trainee
	 * @param week
	 * @return
	 */
	public List<Note> findQCIndividualNotes(Integer traineeId, Integer week) {
		log.debug(FINDING_WEEK + week + " QC individual notes for trainee: " + traineeId);
		return noteDAO.findQCIndividualNotes(traineeId, week);
	}

	/**
	 * FIND ALL WEEKLY BATCH NOTES (VP ONLY)
	 * 
	 * @param batch
	 * @param week
	 * @return
	 */
	public List<Note> findAllBatchNotes(Integer batchId, Integer week) {
		log.debug(FINDING_WEEK + week + " batch notes for batch: " + batchId);
		return noteDAO.findAllBatchNotes(batchId, week);
	}

	/**
	 * Find all qc trainee notes
	 * @return
	 */
	public List<Note> findAllQCTraineeNotes(Integer batchId, Integer week) {
		log.debug("Find All QC Trainee Notes");
		return noteDAO.findAllQCTraineeNotes(batchId, week);
	}
	public List<Note> findAllIndividualNotesOverall(Integer traineeId){
		log.debug("Find Overall notes for trainee " + traineeId);
		return noteDAO.findAllPublicIndividualNotes(traineeId);
	}
	/**
	 * Find all qc trainee notes
	 * @return
	 */
	public List<Note> findAllQCTraineeOverallNotes(Integer traineeId) {
		log.debug("Find All QC Trainee Notes for that trainee");
		return noteDAO.findAllQCTraineeOverallNotes(traineeId);
	}

	/**
     * Find all qc trainee notes for all weeks
     * @return
     */
    public List<List<Note>> findAllQCTraineeNotesForAllWeeks(Integer batchId) {
        log.debug("Find All QC Trainee Notes");
        List<Note> allNotes = noteDAO.findAllQCTraineeNotesForAllWeeks(batchId);
        ArrayList<List<Note>> noteFormatted2d = new ArrayList<List<Note>>();
        List<Note> traineeInfos = new ArrayList<Note>();
        int weekCounter = 0;
        
        if(allNotes==null || allNotes.size()<1) {
            return new ArrayList<List<Note>>();
        }
        int currentId = allNotes.get(0).getTrainee().getTraineeId();
        
        //checking for total number of weeks
        for( int index =0; index<allNotes.size(); index++) {
        	//here we need to find the number of weeks in  total
        	if ( weekCounter < allNotes.get(index).getWeek()){
        		weekCounter = allNotes.get(index).getWeek();
        		System.out.println(weekCounter);
        	}
        }// this function will give us total number of weeks of qc
         if (weekCounter == 1){ //there is only one week on qc notes present
        	 currentId = allNotes.get(0).getTrainee().getTraineeId();
             for( int index =0; index<allNotes.size(); index++) {
                 if(allNotes.get(index).getTrainee().getTraineeId() == currentId) {
                     traineeInfos.add(allNotes.get(index));
                 }else {
                     noteFormatted2d.add(traineeInfos);
                     traineeInfos = new ArrayList<>();
                     if(allNotes.get(index).getTrainee() !=null || allNotes.get(index).getTrainee().getTraineeId() != 0 ) {
                         currentId = allNotes.get(index).getTrainee().getTraineeId();
                         traineeInfos.add(allNotes.get(index));
                     }
                 }if (index == allNotes.size()-1){
                     noteFormatted2d.add(traineeInfos);
                 }
             }
                         
             return noteFormatted2d;
         } //this mehtod will only execute if weeks == 1
        
        //this method should execute if there are more than one week of qc notes
        for( int index =0; index<allNotes.size(); index++) {
        	weekCounter = index + 1; //will throw index out of bouds for last element
        	//need validation for array size
        	if (weekCounter >= allNotes.size()){
        		weekCounter = index;
        	}
        	
	        if((allNotes.get(index).getWeek() != allNotes.get(weekCounter).getWeek())){
	        //***********************************************************************
	            if(allNotes.get(index).getTrainee().getTraineeId() == currentId) {
	                traineeInfos.add(allNotes.get(index));
	            }else {
	                noteFormatted2d.add(traineeInfos);
	                traineeInfos = new ArrayList<>();
	                if(allNotes.get(index).getTrainee() !=null || allNotes.get(index).getTrainee().getTraineeId() != 0 ) {
	                    currentId = allNotes.get(index).getTrainee().getTraineeId();
	                    traineeInfos.add(allNotes.get(index));
	                }
	            }
	            if (index == allNotes.size()-1){
                noteFormatted2d.add(traineeInfos);
	            }
	        }

	        else{
	        	System.out.println("duplicate found");
	        }
	        //**********************************************************************************
	        //**********************************************************************************
        
        }
        return noteFormatted2d;
    }
    
    /**
     * noteDAO.findAllQCBatchNotes
     * @param Integer batchId: the id of the batch 
     * @return A list of QC batch notes, in ascending order by week 
     */
    public List<Note> findAllQCBatchNotes(Integer batchId){
        log.debug("Find All QC Batch Notes in ascending order by week");
        return noteDAO.findAllQCBatchNotes(batchId);
    }
	
}
