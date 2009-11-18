/*
 * Copyright (C) 2009, Miguel Negrão <miguel.negrao _at_ friendlyvirus _dot_ org>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
 
 // 16-05-2009 
 // initial release
 
ToolsMenu {

	*add { |foldersToScan, foldersToShow|
		var tools  = SCMenuGroup.new(nil, "Tools",9),midi, audio, files;
		SCMenuItem.new(tools, "List Nodes").setShortCut( "ä" ).action_({
			"".postln;
			Server.default.name.postln;
			Server.default.queryAllNodes});
		SCMenuSeparator.new(tools);
		SCMenuItem.new(tools, "Start History").action_({
			History.clear.end;
			History.start;
		});
		SCMenuItem.new(tools, "Stop History").action_({
			History.end;
			History.document;
		});
		SCMenuSeparator.new(tools);
		//midi
		midi = SCMenuGroup.new(tools,  "Midi");
		SCMenuItem.new(midi,"Init").action_({
			MIDIClient.init;
			"Connected to:".postln;
			MIDIIn.connect( MIDIClient.sources.at(1).postln);

		});
		SCMenuItem.new(midi, "Check incoming values").action_({
		MIDIIn.control = {arg src, chan, num, val;
				[num,val].postln;
				};
		});
		SCMenuItem.new(midi,"Stop checking incoming values").action_({
			MIDIIn.control = {}
		});
	
		//audio
		audio = SCMenuGroup.new(tools,  "Audio");
		SCMenuItem.new(audio,  "EQ").action_({MasterEQ.new});
		SCMenuItem.new(audio,  "Rec").action_({ServerRecordWindow(Server.default)});
		SCMenuItem.new(audio,  "Sound Card Options").action_({Server.deviceGuis});
		//SCMenuItem.new(audio,  "Init Binaural Buffers").action_({BinAmbi2O.init});
		SCMenuSeparator.new(tools);
		//lang
		SCMenuItem.new(tools,  "Auto Sintax Colorizing").setShortCut("ü").action_({
		Document.current.keyDownAction_{|doc, char, mod, unicode, keycode|
	  		  if(unicode==13 or:(unicode==32) or: (unicode==3)){
	     		   Document.current.syntaxColorize
	   		 }
			}; 
			
		});
		SCMenuItem.new(tools, "Auto Completion").action_({
			Document.current.autoComplete
			});
		SCMenuItem.new(tools,  "Quarks").action_({ Quarks.gui});
		SCMenuItem.new(tools,  "ixiQuarks").action_({ XiiQuarks.new});
		SCMenuItem.new(tools,  "ColorPicker").action_({ColorPicker()});
		SCMenuItem( SCMenuGroup( tools, "Scripts" ), "Run" ).setShortCut( "r" ).action_({ thisProcess.run });

		//SCMenuSeparator.new(tools);
		//Files
		//files = SCMenuGroup.new(tools,  "Files");
		files = SCMenuGroup.new(nil,  "Files");
		
		if(foldersToScan.notNil){
			foldersToScan = foldersToScan.collect{ |path| PathName(path) };
			foldersToScan.do{ |path|
				this.filesMenu(files,path.name,path);
			}
		};
		
		if(foldersToShow.notNil){
			foldersToShow = foldersToShow.collect{ |path| PathName(path) };
			foldersToShow.do { |path|
				//this.filesMenu(nil,path.name,path);
				var fm = SCMenuGroup.new(nil,path.name);
				path.files.do { |path2|
					this.filesMenu(fm,path2.name,path2);
				};
				path.folders.do { |path2|
					this.filesMenu(fm,path2.name,path2);
				};				
			};
		};
	
	}
	
	*filesMenu { arg rootMenu,name, thepath;
//		var temppath;
//		var menuGroup;
		if(thepath.isFolder )
		{ //it's a folder 
			if(thepath.folderName.extension == "rtfd")
			{ var temppath = thepath.fullPath;
				//temppath = thepath.fullPath;
				temppath.pop;
				SCMenuItem.new(rootMenu,thepath.folderName).action_({Document.open(temppath) })
			}
			{
				var menuGroup = SCMenuGroup.new(rootMenu, name);
				//menuGroup = SCMenuGroup.new(rootMenu, name);
				thepath.files.do{ |path|
					SCMenuItem.new(menuGroup,  path.fileName).action_({
						if((path.extension == "rtf") ||
							(path.extension == "sc") ||
							(path.extension == "scd") ||
							(path.extension == "html")) {
							Document.open(path.fullPath);
						}
						{
							("open " ++ "'" ++ path.fullPath ++ "'"	 ++ "").unixCmd;
						}
					});
				};
				thepath.folders.do{ |path|
					this.filesMenu(menuGroup,path.folderName,path)
				};
			}
		}		
		{ SCMenuItem.new(rootMenu,thepath.fileName).action_({Document.open(thepath.fullPath) }) }
		
	}
	
}

+ PathName {
	
	name{
	if(this.isFolder){^this.folderName };
	if(this.isFile){ ^this.fileName };
	}
	
}


// change the path to point to a file you want to run with cmd^run
//+ Main {
//		run { "/Users/peter/Library/Application Support/SuperCollider/startup.rtf".load }
//}



// change the path to point to a file you want to run with cmd^run
+ Main {
		run { "/Users/peter/Library/Application Support/SuperCollider/Extensions/RedFolderGUI/RedFolderGUIStarter.rtf".load }
}
			