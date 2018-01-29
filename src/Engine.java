import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import download.WebService;
import object.Artist;
import parsers.ParseResultsForWS;
import parsers.WebServiceDescription;

public class Engine {
    //public static HashMap<String, Artist> artists = new HashMap<String, Artist>();


    public static final void main(String[] args) throws Exception{

        String querys = "getArtistInfoByName(?name=Kuraki,?artistId,beginDate,endDate)#getAlbumByArtistId(?artistId,albumId,albumName)#getSongByAlbumId(?albumId,songName)";
        
        String[] services =querys.split("#");
        String function =null;
        String[] params=null;
        String input=null;
        WebService ws;
        String fileWithCallResult;
        String fileWithTransfResults=null;
       // Objt result = new Objt();
        List<Artist> artists=new ArrayList<Artist>();
        if(services.length>0){
            function = services[0].split("[()]")[0];
            params = getParams(services[0]);
            input = getInput(services[0]);

            //System.out.println(Arrays.toString(params));
            //System.out.println(function);
            //System.out.println(input);
            
            ws = WebServiceDescription.loadDescription("mb_"+function);
            fileWithCallResult = ws.getCallResult(input);
    		System.out.println("The call is   **"+fileWithCallResult+"**");

            fileWithTransfResults=null;
            
            ArrayList<String[]> res1 = null;
            try {
			fileWithTransfResults=ws.getTransformationResult(fileWithCallResult);
			res1=ParseResultsForWS.showResults(fileWithTransfResults, ws);
			System.out.println("The result of the first Web service call:");
			System.out.println("Artist Name"+"                  "+"Artist ID");
            } catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
            }
            for(String[] r:res1){
            	//System.out.println(r[3]);
            	//System.out.println(Arrays.toString(r));
            	Artist artist=new Artist(r[1], r[0], r[2], r[3]);
            	artists.add(artist);
            	System.out.println(r[0]+"                  "+r[1]);
            }
            System.out.println();
            int i;
            for(i=1;i<services.length;i++)
            {	
            	function=services[i].split("[()]")[0];
            	params=getParams(services[i]);
            	input=getInput(services[i]);
            	//System.out.println(input);
                //ws = WebServiceDescription.loadDescription("mb_"+function);
                if(input=="None")
                {
                	if(artists.size()>0)
                	{
                		for(Artist a:artists)
                		{	
                			if(i==1)
                				{
                					input=a.artistId;
                					ws = WebServiceDescription.loadDescription("mb_"+function);
                        			fileWithCallResult = ws.getCallResult(input);
                        			
                            		System.out.println("The call is   **"+fileWithCallResult+"**");
                            		ArrayList<String[]> res2 = null;
                                    try {
                        			fileWithTransfResults=ws.getTransformationResult(fileWithCallResult);
                        			res2=ParseResultsForWS.showResults(fileWithTransfResults, ws);
                        			System.out.println("The result of the second Web service call after join with the first web service:");
                        			System.out.println("Artist ID"+"                                      "+"Artist NAME"+"                                       "+"Albums ID"+"                               "+"Albums Name");
                                    } catch (Exception e) {
                        			// TODO: handle exception
                        			e.printStackTrace();
                                    }
                                    for(String[] r:res2){
                                    	//System.out.println(r[3]);
                                    	if(!a.ablums.containsKey(r[1]))
                                    		a.addAblums(r[1],r[2]);
                                    		System.out.println(input+"                  "+a.artistName+"                  "+r[1]+"                  "+r[2]);
                                    }
                                    
                				}
                			else{
                				for(String key:a.ablums.keySet())
                				{	
                					input=key;
                					ws = WebServiceDescription.loadDescription("mb_"+function);
                        			fileWithCallResult = ws.getCallResult(input);
                        			
                            		System.out.println("The call is   **"+fileWithCallResult+"**");
                            		ArrayList<String[]> res2 = null;
                                    try {
                        			fileWithTransfResults=ws.getTransformationResult(fileWithCallResult);
                        			res2=ParseResultsForWS.showResults(fileWithTransfResults, ws);
                                    } catch (Exception e) {
                        			// TODO: handle exception
                        			e.printStackTrace();
                                    }
                                   //System.out.println(res2);
                                        for(String[] r:res2){
                                        	a.addSongs(r[0], r[1]);
                                        }
                                    
                				}
                			}
                			
                		}
                	}
                }
                
            }
            System.out.println("----------------------Result-----------------------");
            System.out.println("ArtistName"+"              "+"Albums name"+"              "+"Song Name");
            for(Artist a:artists)
            {	
            	for(String ablum:a.songs.keySet())
            	{	//System.out.println("test:"+ablum);
            		for(String song:a.songs.get(ablum))
            			System.out.println(a.artistName+"         "+ablum+"         "+song);
            	}
            }
            
        }
        

    }


	private static String getInput(String query) {
        Pattern patttern = Pattern.compile("name=(.*?),", Pattern.CASE_INSENSITIVE);
        Matcher matcher = patttern.matcher(query);
        if(matcher.find())
            return matcher.group(1);
        else
            return "None";
	}


	private static String[] getParams(String query) {
        String parameters = query.split("[()]")[1];
        String[] parameter = parameters.split(",");
        Pattern pat = Pattern.compile("=.*", Pattern.CASE_INSENSITIVE);
        for(int i=0;i < parameter.length;i++){
            Matcher m = pat.matcher(parameter[i]);
            parameter[i] = m.replaceAll("");
            parameter[i] = parameter[i].replace("?", "");
        }
        return parameter;
	}

}
