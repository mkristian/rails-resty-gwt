require 'fileutils'
require File.join(File.dirname(__FILE__), 'ruby_maven')

def rmvn
  @rmvn ||= Maven::RubyMaven.new
end

def copy_tests(tests)
  FileUtils.mkdir_p(@app_directory)
  FileUtils.cp_r(File.join('templates', "tests-#{tests}", "."), 
                 File.join(@app_directory, 'test'))
end

def copy_specs(specs)
  FileUtils.mkdir_p(@app_directory)
  FileUtils.cp_r(File.join('templates', "specs-#{specs}", "."), 
                 File.join(@app_directory, 'spec'))
end

def create_rails_application(template)
  name = template.sub(/.template$/, '')
  @app_directory = File.join('target', name)

  # rails version from gemspec
  gemspec = File.read(Dir.glob("*.gemspec")[0])
  rails_version = gemspec.split("\n").detect { |l| l =~ /development_dep.*rails/ }.sub(/'$/, '').sub(/.*'/, '')
  
  rmvn.options['-Dplugin.version'] = '0.28.4-SNAPSHOT'
  rmvn.options['-Drails.version'] = rails_version
  rmvn.options['-Dgem.home'] = ENV['GEM_HOME']
  rmvn.options['-Dgem.path'] = ENV['GEM_PATH']
  rmvn.options['-o'] = nil

  FileUtils.rm_rf(@app_directory)

  rmvn.exec("rails", "new", @app_directory, "-f")

  # TODO that should be done via the rails new task !!!
  rmvn.exec_in(@app_directory, "rails", "rake", "rails:template LOCATION=" + File.expand_path("templates/#{template}"))
end

Given /^I create new rails application with template "(.*)"$/ do |template|
  create_rails_application(template)
end

Given /^I create new rails application with template "(.*)" and "(.*)" tests$/ do |template, tests|
  create_rails_application(template)
  copy_tests(tests)
end

Given /^I create new rails application with template "(.*)" and "(.*)" specs$/ do |template, specs|
  create_rails_application(template)
  copy_specs(specs)
end

Given /^me an existing rails application "(.*)"$/ do |name|
  @app_directory = File.join('target', name)
end

Given /^me an existing rails application "(.*)" and "(.*)" tests$/ do |name, tests|
  @app_directory = File.join('target', name)
  copy_tests(tests)
end

Given /^me an existing rails application "(.*)" and "(.*)" specs$/ do |name, specs|
  @app_directory = File.join('target', name)
  copy_specs(specs)
end

And /^I execute \"(.*)\"$/ do |args|
  rmvn.options['-l'] = "output.log"
  rmvn.exec_in(@app_directory, args)
end

Then /^the output should contain \"(.*)\"$/ do |expected|
  result = File.read(File.join(@app_directory, "output.log"))
  expected.split(/\"?\s+and\s+\"?/).each do |exp|
    puts exp
    (result =~ /.*#{exp}.*/).should_not be_nil
  end
end

