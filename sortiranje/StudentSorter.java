package hr.java.vjezbe.sortiranje;

import java.util.Comparator;

import hr.java.vjezbe.entitet.Student;

public class StudentSorter implements Comparator<Student> {

	public StudentSorter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compare(Student st1, Student st2) {
		
		Integer prezimeResult;
		prezimeResult = st1.getPrezime().compareTo(st2.getPrezime()); 
		if (prezimeResult == 0) {
			return st1.getIme().compareTo(st2.getIme());
		}
		else return prezimeResult;
		
	}

}
