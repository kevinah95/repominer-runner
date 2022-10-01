package org.example;

import org.repositoryminer.RepositoryExtractor;
import org.repositoryminer.RepositoryMiner;
import org.repositoryminer.domain.SCMType;
import org.repositoryminer.metrics.MetricsConfig;
import org.repositoryminer.metrics.RepositoryMinerMetrics;
import org.repositoryminer.metrics.codemetric.CYCLO;
import org.repositoryminer.metrics.codemetric.CodeMetric;
import org.repositoryminer.metrics.codemetric.LOC;
import org.repositoryminer.metrics.codesmell.CodeSmell;
import org.repositoryminer.metrics.codesmell.GodClass;
import org.repositoryminer.metrics.codesmell.LongMethod;
import org.repositoryminer.metrics.parser.Parser;
import org.repositoryminer.metrics.parser.java.JavaParser;
import org.repositoryminer.persistence.MongoConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        // Connects with the database.
        MongoConnection conn = MongoConnection.getInstance();
        conn.connect("mongodb://localhost", "test_database");

        // This class is the main interface of core module.
        RepositoryMiner rm = new RepositoryMiner("junit4");

        // Here we set the basic repository informations.
        // Pay attention to rm.setRepositoryKey, in this method you will
        // set an unique identifier for the repository.
        rm.setKey("junit4");
        rm.setName("Junit4");
        rm.setDescription("A programmer-oriented testing framework for Java.");
        rm.setPath("/Users/khernandezr/Documents/workshop/kevs/centic9/jgit-cookbook");

        // Here we set the SCM.
        rm.setSCM(SCMType.GIT);

        // The steps below are optional, if you no want to do any code analysis just
        // call rm.mine method.

        // Here we set the parser for the programming languages.
        List<Parser> parsers = new ArrayList<>();
        parsers.add(new JavaParser());

        // Here we set the software metrics.
        List<CodeMetric> metrics = Arrays.asList(new LOC(), new CYCLO());

        // Here we set the code smells.
        List<CodeSmell> codeSmells = Arrays.asList(new GodClass(), new LongMethod());

        MetricsConfig metricsConfig = new MetricsConfig(parsers, metrics, codeSmells);

        // Here we set the references(tag or branches) that we want to perform the code
        // analysis.
        RepositoryExtractor.run(rm);

        // Calc metrics
        //RepositoryMinerMetrics repositoryMinerMetrics = new RepositoryMinerMetrics();
        //repositoryMinerMetrics.init("junit4");
        //repositoryMinerMetrics.run("9ecaddcb98098327faeee2bbcb725abcdb714d0d", metricsConfig);

        // This method starts the mining.
        rm.mine();
    }
}