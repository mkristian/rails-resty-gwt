# -*- mode: ruby -*-
Gem::Specification.new do |s|
  s.name = 'resty-generators'
  s.version = '0.6.0'

  s.summary = 'rails3 generators which use restygwt to communicate from GWT with rails'
  s.description = 'simple authorization framework for rails controllers'
  s.homepage = 'http://github.com/mkristian/ixtlan-guard'

  s.authors = ['mkristian']
  s.email = ['m.kristian@web.de']

  s.licenses << 'MIT-LICENSE'
  s.files = Dir['MIT-LICENSE']
  s.files += Dir['History.txt']
  s.files += Dir['lib/**/*']
  s.files += Dir['spec/**/*']
  s.files += Dir['features/**/*rb']
  s.files += Dir['features/**/*feature']
  s.test_files += Dir['spec/**/*_spec.rb']
  s.test_files += Dir['features/*.feature']
  s.test_files += Dir['features/step_definitions/*.rb']
  s.add_dependency 'ixtlan-generators', '~>0.1.1'
  s.add_development_dependency 'rails', '3.0.9'
  s.add_development_dependency 'rspec', '2.6.0'
  s.add_development_dependency 'cucumber', '0.9.4'
  s.add_development_dependency 'rake', '0.8.7'
  s.add_development_dependency 'ruby-maven', '0.8.3.0.3.0.28.3'
end

# vim: syntax=Ruby
