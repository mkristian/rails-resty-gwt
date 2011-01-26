plugin('org.codehaus.mojo.gwt-maven-plugin', '2.1.0') do |gwt|
  gwt.with({ :logLevel => "INFO",
             :style => "DETAILED",
             :treeLogger => true,
             :extraJvmArgs => "-Xmx512m",
             :runTarget => "<%= application_name.underscore %>/<%= application_name.underscore %>.html",
             :includes => "**/<%= application_name %>GWTTestSuite.java"
           })
  gwt.execute.goals << ["clean", "compile", "test"]
end
#-- Macs need the -d32 -XstartOnFirstThread jvm options -->
profile("mac") do |mac|
  mac.activation << "<os><family>mac</family></os>"
  plugin('org.codehaus.mojo.gwt-maven-plugin').with(:extraJvmArgs => "-d32 -XstartOnFirstThread -Xmx512m")
end
