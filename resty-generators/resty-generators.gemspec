# -*- mode: ruby -*-
Gem::Specification.new do |s|
  s.name = 'resty-generators'
  s.version = '0.2.0'

  s.summary = 'guard your controller actions'
  s.description = 'simple authorization framework for rails controllers'
  s.homepage = 'http://github.com/mkristian/ixtlan-guard'

  s.authors = ['mkristian']
  s.email = ['m.kristian@web.de']

  s.licenses << 'MIT-LICENSE'
  s.files = Dir['MIT-LICENSE']
  s.files += Dir['History.txt']
  s.files += Dir['README.textile']
#  s.extra_rdoc_files = ['History.txt','README.textile']
  s.rdoc_options = ['--main','README.textile']
  s.files += Dir['lib/**/*']
  s.files += Dir['spec/**/*']
  s.files += Dir['features/**/*rb']
  s.files += Dir['features/**/*feature']
  s.test_files += Dir['spec/**/*_spec.rb']
  s.test_files += Dir['features/*.feature']
  s.test_files += Dir['features/step_definitions/*.rb']
  s.add_dependency 'ixtlan-core', '~>0.2.0'
  s.add_development_dependency 'rails', '3.0.5'
  #s.add_development_dependency 'rspec', '2.0.1'
  s.add_development_dependency 'cucumber', '0.10.2'
  s.add_development_dependency 'rake', '0.8.7'
  s.add_development_dependency 'ruby-maven', '0.8.3.0.2.0.26.0'
end

# vim: syntax=Ruby
