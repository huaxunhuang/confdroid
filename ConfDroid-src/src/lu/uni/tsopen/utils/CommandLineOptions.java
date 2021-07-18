/*     */ package lu.uni.tsopen.utils;
/*     */ 
/*     */ import org.apache.commons.cli.CommandLine;
/*     */ import org.apache.commons.cli.CommandLineParser;
/*     */ import org.apache.commons.cli.DefaultParser;
/*     */ import org.apache.commons.cli.HelpFormatter;
/*     */ import org.apache.commons.cli.Option;
/*     */ import org.apache.commons.cli.Options;
/*     */ import org.apache.commons.cli.ParseException;
/*     */ import org.javatuples.Triplet;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommandLineOptions
/*     */ {
/*  49 */   private static final Triplet<String, String, String> FILE = new Triplet("file", "f", "Apk file");
/*  50 */   private static final Triplet<String, String, String> HELP = new Triplet("help", "h", "Print this message");
/*  51 */   private static final Triplet<String, String, String> TIMEOUT = new Triplet("timeout", "t", "Set a timeout in minutes (60 by default) to exit the application");
/*     */   
/*  53 */   private static final Triplet<String, String, String> PLATFORMS = new Triplet("platforms", "p", "Android platforms folder");
/*     */   
/*  55 */   private static final Triplet<String, String, String> EXCEPTIONS = new Triplet("exceptions", "e", "Take exceptions into account during full path predicate recovery");
/*     */   
/*  57 */   private static final Triplet<String, String, String> OUTPUT = new Triplet("output", "o", "Output results in given file");
/*     */   
/*  59 */   private static final Triplet<String, String, String> QUIET = new Triplet("quiet", "q", "Do not output results in console");
/*     */   
/*  61 */   private static final Triplet<String, String, String> CALLGRAPH = new Triplet("callgraph", "c", "Define the call-graph algorithm to use (SPARK, CHA, RTA, VTA)");
/*     */   
/*  63 */   private static final Triplet<String, String, String> RAW = new Triplet("raw", "r", "write raw results in stdout");
/*     */   
/*     */   private static final String TSOPEN = "TSOpen";
/*     */   private Options options;
/*     */   private Options firstOptions;
/*     */   private CommandLineParser parser;
/*     */   private CommandLine cmdLine;
/*     */   private CommandLine cmdFirstLine;
/*  71 */   private Logger logger = LoggerFactory.getLogger(getClass());
/*     */   
/*     */   public CommandLineOptions(String[] args) {
/*  74 */     this.options = new Options();
/*  75 */     this.firstOptions = new Options();
/*  76 */     initOptions();
/*  77 */     this.parser = (CommandLineParser)new DefaultParser();
/*  78 */     parse(args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parse(String[] args) {
/*  87 */     HelpFormatter formatter = null;
/*     */     try {
/*  89 */       this.cmdFirstLine = this.parser.parse(this.firstOptions, args, true);
/*  90 */       if (this.cmdFirstLine.hasOption((String)HELP.getValue0())) {
/*  91 */         formatter = new HelpFormatter();
/*  92 */         formatter.printHelp("TSOpen", this.options, true);
/*  93 */         System.exit(0);
/*     */       } 
/*  95 */       this.cmdLine = this.parser.parse(this.options, args);
/*  96 */     } catch (ParseException e) {
/*  97 */       this.logger.error(e.getMessage());
/*  98 */       System.exit(1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initOptions() {
/* 112 */     Option file = Option.builder((String)FILE.getValue1()).longOpt((String)FILE.getValue0()).desc((String)FILE.getValue2()).hasArg(true).argName((String)FILE.getValue0()).required(true).build();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 120 */     Option platforms = Option.builder((String)PLATFORMS.getValue1()).longOpt((String)PLATFORMS.getValue0()).desc((String)PLATFORMS.getValue2()).hasArg(true).argName((String)PLATFORMS.getValue0()).required(true).build();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 126 */     Option help = Option.builder((String)HELP.getValue1()).longOpt((String)HELP.getValue0()).desc((String)HELP.getValue2()).argName((String)HELP.getValue0()).build();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 132 */     Option exceptions = Option.builder((String)EXCEPTIONS.getValue1()).longOpt((String)EXCEPTIONS.getValue0()).desc((String)EXCEPTIONS.getValue2()).argName((String)EXCEPTIONS.getValue0()).build();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     Option timeout = Option.builder((String)TIMEOUT.getValue1()).longOpt((String)TIMEOUT.getValue0()).desc((String)TIMEOUT.getValue2()).argName((String)TIMEOUT.getValue0()).hasArg(true).build();
/* 140 */     timeout.setOptionalArg(true);
/* 141 */     timeout.setType(Number.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 149 */     Option output = Option.builder((String)OUTPUT.getValue1()).longOpt((String)OUTPUT.getValue0()).desc((String)OUTPUT.getValue2()).hasArg(true).argName((String)OUTPUT.getValue0()).build();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 155 */     Option quiet = Option.builder((String)QUIET.getValue1()).longOpt((String)QUIET.getValue0()).desc((String)QUIET.getValue2()).argName((String)QUIET.getValue0()).build();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 161 */     Option raw = Option.builder((String)RAW.getValue1()).longOpt((String)RAW.getValue0()).desc((String)RAW.getValue2()).argName((String)RAW.getValue0()).build();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 168 */     Option callgraph = Option.builder((String)CALLGRAPH.getValue1()).longOpt((String)CALLGRAPH.getValue0()).desc((String)CALLGRAPH.getValue2()).argName((String)CALLGRAPH.getValue0()).hasArg(true).build();
/* 169 */     timeout.setOptionalArg(true);
/*     */     
/* 171 */     this.firstOptions.addOption(help);
/*     */     
/* 173 */     this.options.addOption(file);
/* 174 */     this.options.addOption(platforms);
/* 175 */     this.options.addOption(exceptions);
/* 176 */     this.options.addOption(timeout);
/* 177 */     this.options.addOption(output);
/* 178 */     this.options.addOption(quiet);
/* 179 */     this.options.addOption(callgraph);
/* 180 */     this.options.addOption(raw);
/*     */     
/* 182 */     for (Option o : this.firstOptions.getOptions()) {
/* 183 */       this.options.addOption(o);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getFile() {
/* 188 */     return this.cmdLine.getOptionValue((String)FILE.getValue0());
/*     */   }
/*     */   
/*     */   public String getPlatforms() {
/* 192 */     return this.cmdLine.getOptionValue((String)PLATFORMS.getValue0());
/*     */   }
/*     */   
/*     */   public boolean hasExceptions() {
/* 196 */     return this.cmdLine.hasOption((String)EXCEPTIONS.getValue1());
/*     */   }
/*     */   
/*     */   public boolean hasOutput() {
/* 200 */     return this.cmdLine.hasOption((String)OUTPUT.getValue1());
/*     */   }
/*     */   
/*     */   public String getOutput() {
/* 204 */     return this.cmdLine.getOptionValue((String)OUTPUT.getValue0());
/*     */   }
/*     */   
/*     */   public boolean hasQuiet() {
/* 208 */     return this.cmdLine.hasOption((String)QUIET.getValue1());
/*     */   }
/*     */   
/*     */   public boolean hasRaw() {
/* 212 */     return this.cmdLine.hasOption((String)RAW.getValue1());
/*     */   }
/*     */   
/*     */   public int getTimeout() {
/* 216 */     Number n = null;
/*     */     try {
/* 218 */       n = (Number)this.cmdLine.getParsedOptionValue((String)TIMEOUT.getValue1());
/* 219 */       if (n == null) {
/* 220 */         return 0;
/*     */       }
/* 222 */       return n.intValue();
/*     */     }
/* 224 */     catch (Exception exception) {
/* 225 */       return 0;
/*     */     } 
/*     */   }
/*     */   public String getCallGraph() {
/* 229 */     String cg = this.cmdLine.getOptionValue((String)CALLGRAPH.getValue0());
/* 230 */     if (cg != null && (
/* 231 */       cg.equals("SPARK") || cg.equals("CHA") || cg.equals("RTA") || cg.equals("VTA"))) {
/* 232 */       return cg;
/*     */     }
/*     */     
/* 235 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/name1/Documents/ConfDroid.jar!/lu/uni/tsopen/utils/CommandLineOptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */