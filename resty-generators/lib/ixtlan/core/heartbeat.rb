class Ixtlan::Core::Heartbeat

  class Resource

    attr_reader :local, :remote
    attr_accessor :count, :failure

    def initialize(local, remote)
      @count = 0
      @failures = 0
      @local = local
      @remote = remote
    end

    def to_log
      "update #{@local} - total: #{@count + @failures}  success: #{@count}  failures: #{@failures}"
    end
  end

  def initialize
    @count = 0
    @failures = 0
  end

  def reg
    @reg ||= {}
  end

  def register(local, remote)
    reg[local] = Resource.new(local, remote)
  end

  def beat(resource = nil)
    resources = resource.nil? ? reg.values : [reg[resource]]
    resources.each do |res|
      last_date = res.local.maximum(:updated_at) || 2000.years.ago
      last_update = last_date.strftime('%Y-%m-%d %H:%M:%S.') + ("%06d" % last_date.usec)
      res.remote.get(:last_changes, :updated_at => last_update).each do |remote|
        id = remote.delete('id')
        u = res.local.find_by_id(id)
        result = if u
                   u.update_attributes(remote)
                 else
                   u = res.local.new(remote)
                   u.id = id 
                   u.save
                 end
        if result
          res.count = res.count + 1
        else
          res.failures = res.failures + 1
        end
      end
    end
  end
  def to_log
    reg.values.collect { |r| r.to_log }.join("\n\t")
  end
  alias :to_s :to_log
end
