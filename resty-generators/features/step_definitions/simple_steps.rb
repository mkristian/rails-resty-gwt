require 'fileutils'

def create_rails_application(template, tests = nil)
  name = template.sub(/.template$/, '')
  app_directory = File.join('target', name)

  rails_version = '3.0.5'

  command = File.join("#{ENV['GEM_HOME']}", 'bin','rmvn')
  jruby = File.read(command).split("\n")[0].sub(/^#!/, '')
  args = "rails new #{app_directory} -f -m #{File.join('templates',template)} -- -DrailsVersion=#{rails_version} -Dgem.home=#{ENV['GEM_HOME']} -Dgem.path=#{ENV['GEM_PATH']}"# -Dplugin.version=0.26.0-SNAPSHOT"
  FileUtils.rm_rf(app_directory)

  if tests
    FileUtils.mkdir_p(app_directory)
    FileUtils.cp_r(File.join('templates', "tests-#{tests}"), 
                   File.join(app_directory, 'test'))
  end

  full = "#{jruby} #{command} #{args}"
  # puts full
  system full
  
  @result = File.read(File.join(app_directory, "output.log"))
  puts @result
end

Given /^I create new rails application with template "(.*)"$/ do |template|
  create_rails_application(template)
end

Given /^I create new rails application with template "(.*)" and "(.*)" tests$/ do |template, tests|
  create_rails_application(template, tests)
end

Then /^the output should contain \"(.*)\"$/ do |expected|
  expected.split(/\"?\s+and\s+\"?/).each do |exp|
    puts exp
    (@result =~ /.*#{exp}.*/).should_not be_nil
  end
end

