package bwg4.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import bwg4.api.biome.BiomeManager;
import net.minecraft.world.biome.BiomeGenBase;

public class GuiBiomeSettingsInfo
{
    private final List Biomelist = new ArrayList();
	
    public List theBiomesList()
    {
        return this.Biomelist;
    }	
	
    public static GuiBiomeSettingsInfo defaultBiomesList()
    {
        return FromString(BiomeManager.getDefaultString());
	}
	
    public String toString()
    {
		//CREATE STRING
        StringBuilder genstring = new StringBuilder();
		
		//VERSION
        genstring.append("2&");
		
		//GET BIOME INFO
		boolean first = true;
        for (int var2 = 0; var2 < this.Biomelist.size(); ++var2)
        {
        	if(((BiomeInfo)this.Biomelist.get(var2)).getEnabled())
        	{
	            if (!first)
	            {
	            	genstring.append(";");
	            }
	            else
	            {
	                first = false;
	            }
	
	            genstring.append(((BiomeInfo)this.Biomelist.get(var2)).toString());
        	}
        }

		return genstring.toString();
	}
	
	public static GuiBiomeSettingsInfo FromString(String par0Str)
    {
		GuiBiomeSettingsInfo var0 = new GuiBiomeSettingsInfo();
        if (par0Str != "")
        {
			String[] mainarray = par0Str.split("&");
			if(Integer.parseInt(mainarray[0]) == 1)
			{
				String[] biomesarray = mainarray[1].split(";");
				String settingName;
				boolean enabled;
				
				for(int biome = 0; biome < biomesarray.length; ++biome)
				{
					String[] biomeINFO = biomesarray[biome].split("=");
					settingName = biomeINFO[0];
	
					if(biomeINFO[1].equals("true"))
					{
						enabled = true;
					}
					else
					{
						enabled = false;
					}
					
					var0.theBiomesList().add(new BiomeInfo(settingName, enabled)); 
				}
			}
			else
			{
				String[] deflist = BiomeManager.getStringList();
				String[] biomesarray = mainarray[1].split(";");
				String settingName;
				boolean enabled;
				
				for(int b = 0; b < deflist.length; b++)
				{
					if(deflist[b] != null)
					{
						enabled = false;
						for(int biome = 0; biome < biomesarray.length; ++biome)
						{
							if(deflist[b] != null)
							{
								if(biomesarray[biome].equals(deflist[b])) 
								{ 
									enabled = true;
								}
							}
						}
						var0.theBiomesList().add(new BiomeInfo(deflist[b], enabled)); 
					}
				}
			}
			return var0;
		}
		return defaultBiomesList();
	}
}