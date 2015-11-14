package csgodemo;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import io.apigee.trireme.core.NodeEnvironment;
import io.apigee.trireme.core.NodeException;
import io.apigee.trireme.core.NodeScript;
import io.apigee.trireme.core.ScriptStatus;

public class DemoMain {
	
	public static void main (String[] args) throws NoSuchMethodException, IOException, InterruptedException, ScriptException, NodeException, ExecutionException{

		new UI();
		
	}

}
