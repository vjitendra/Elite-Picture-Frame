package com.EFrame11;

public class Session {

	private static String album_name="";
	private static String selected_photo="";
	private static String elitePictureFrameId="";
	private static String musicSelected="";
	private static String currentClass="";
	private static String []paths;
	private static int count=0;
	
	public static void setSessionAlbumName(String album_name) {
	    Session.album_name = album_name;
	}

	public static String getSessionAlbumName() {
	    return album_name;
	}

	public static void setSessionSelectedPhoto(String selected_photo) {
	    Session.selected_photo = selected_photo;
	}

	public static String getSessionSelectedPhoto() {
	    return selected_photo;
	}
			
	public static void setSessionElitePictureFrameId(String elitePictureFrameId) {
	    Session.elitePictureFrameId = elitePictureFrameId;
	}

	public static String getSessionElitePictureFrameId() {
	    return elitePictureFrameId;
	}
	
	public static void setSessionMusicSelected(String musicSelected) {
	    Session.musicSelected = musicSelected;
	}

	public static String getSessionMusicSelected() {
	    return musicSelected;
	}
	
	public static void setSessionCurrentClass(String currentClass) {
	    Session.currentClass = currentClass;
	}

	public static String getSessionCurrentClass() {
	    return currentClass;
	}
	
	
	
	public static void setSessionImageTotDownload(String []imageTotDownload) {
		
		paths = new String[imageTotDownload.length];
		
		for(int i=0; i<imageTotDownload.length; i++)
			Session.paths[i] = imageTotDownload[i];
	}

	public static String getSessionImageTotDownload() {
		count--;
		return paths[count];
	}
	
	public static void setSessionCount(int count) {
	    Session.count = count;
	}

	public static int getSessionCount() {
	    return count;
	}
	
	public static int getSessionPathsCount() {
	    return paths.length;
	}
	
	public static String getSessionAllPaths(int i) {
	    return paths[i];
	}
}
