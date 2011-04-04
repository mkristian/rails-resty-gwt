#-*- mode: ruby -*-

def run_generate(*args)
  run_rmvn("rails", "generate", *args)
end

def run_rmvn(goal, *args)
  separator = args.member?('--') ? ' ' : ' -- '
  run "rmvn #{goal} #{args.join(' ')}#{separator}-Dgem.home=#{ENV['GEM_HOME']} -Dgem.path=#{ENV['GEM_PATH']} >> output.log"
end

def run_rails(command, *args)
  run_rmvn("rails", command, *args)
end

def run_rake(*args)
  run_rmvn("rails", "rake", *args)
end
