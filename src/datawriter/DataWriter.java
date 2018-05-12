/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datawriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author me
 */
public class DataWriter {

    private static LogManager LM = new LogManager();
    private static Logger LOG = LM.getLog();

    public static void main(String args[]) throws InterruptedException {

        LOG.info("Starting.  Processing command-line parameters.");

        CommandLineParser parser = new DefaultParser();
        Options programOptions = new Options();

        Option myOption = Option.builder("i")
                .longOpt("txInput")
                .desc("Folder Name for incoming transactions, written by this pgm")
                .hasArg()
                .argName("txInput")
                .build();
        programOptions.addOption(myOption);

        myOption = Option.builder("o")
                .longOpt("txOutput")
                .desc("Folder Name for Sent transactions")
                .hasArg()
                .argName("txOutput")
                .build();
        programOptions.addOption(myOption);

        myOption = Option.builder("b")
                .longOpt("beat")
                .desc("Heartbeat interval in ms")
                .hasArg()
                .argName("heartBeat")
                .build();
        programOptions.addOption(myOption);

        myOption = Option.builder("c")
                .longOpt("Count")
                .desc("Max transactions to send")
                .hasArg()
                .argName("MaxCount")
                .build();

        programOptions.addOption(myOption);

        String txInputName = "tx.in";
        String txOutputName = "tx.out";
        int heartBeat = 60; // in ms
        int maxCount = 1;

        CommandLine cmd;
        try {
            cmd = parser.parse(programOptions, args);

            if (cmd.hasOption("c")) {
                LOG.info("Setting max count to " + cmd.getOptionValue("Count"));
                maxCount = Integer.parseInt(cmd.getOptionValue("Count"));
            } else {
                LOG.info("Max Transaction Count defaults to " + maxCount);
            }
            if (cmd.hasOption("b")) {
                LOG.info("Setting heartbeat interval to " + cmd.getOptionValue("beat") + "ms.");
                heartBeat = Integer.parseInt(cmd.getOptionValue("beat"));
            } else {
                LOG.info("Heartbeat interval defaults to " + heartBeat);
            }

            // Make the input and output folders if they don't exist.
            if (cmd.hasOption("txInput")) {
                txInputName = cmd.getOptionValue("txInput");
            }
            File txInput = new File(String.valueOf(txInputName));
            if (!txInput.exists()) {
                txInput.mkdir();
            }

            if (cmd.hasOption("txOutput")) {
                txOutputName = cmd.getOptionValue("txOutput");
            }
            File txOutput = new File(String.valueOf(txOutputName));
            if (!txOutput.exists()) {
                txOutput.mkdir();
            }

            for (int i = 0; i < maxCount; i++) {
                PrintWriter writer;
                try {
                    String fn = txInputName.toString() + File.separator + "file" + String.format("%02d", i);
                    LOG.info("File name is " + fn);
                    
                    writer = new PrintWriter(fn, "UTF-8");
                    writer.println("The first line");
                    writer.close();
                    Thread.sleep(heartBeat);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(DataWriter.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(DataWriter.class.getName()).log(Level.SEVERE, null, ex);

                }
            }
        } catch (Exception ex) {
            Logger.getLogger(DataWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
// Main 
}
