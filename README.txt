On server:
	1. Run baseCommands/runServerStart.bat to open connection to the base.
	2. Run out/artifacts/JigsawServer_jar/Jigsaw.jar      (Or JigsawServerRun.main)
		Arguments:
			{Number of players} {Server Port} {Max duration of each game}
              		java -jar Jigsaw.jar 2 5000 300
	3. After finishing the game and closing the Jigsaw server run
	   baseCommands/runServerShutdown.bat
On client:
	1. Run out/artifacts/JigsawClient_jar/Jigsaw.jar      (Or JigsawClientRun.main)
		Arguments:
			{Host name} {Server Port} {Player's (your) name}
			java -jar Jigsaw.jar localhost 5000 Kostichka