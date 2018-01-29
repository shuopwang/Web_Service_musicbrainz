package object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hp.hpl.jena.tdb.store.Hash;

public class Artist {
	public String artistId;
	public String artistName;
	String beginDate;
	String endDate;
	public HashMap<String,String> ablums=new HashMap();
	public HashMap<String,ArrayList<String>> songs=new HashMap();
	public Artist(String id,String name,String begin,String end)
	{
		this.artistId=id;
		this.artistName=name;
		this.beginDate=begin;
		this.endDate=end;
	}
	public void addAblums(String ablumid,String ablum)
	{
		
		this.ablums.put(ablumid, ablum);
	}
	public void addSongs(String ablumid,String song)
	{	
		if(ablums.containsKey(ablumid))
		{
			String album=ablums.get(ablumid);
			if(songs.containsKey(album))
			{
				ArrayList<String> sings=songs.get(album);
				sings.add(song);
				songs.put(album, sings);
			}
			else{
				ArrayList<String> sings=new ArrayList<>();
				if(song!=null)
					{sings.add(song);
					songs.put(album, sings);}
			}
		}
	}

}
