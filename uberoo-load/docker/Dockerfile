FROM denvazh/gatling:latest
ADD ./Simulation.scala /opt/gatling/user-files/simulations
RUN ls /opt/gatling/user-files/simulations
CMD ["-s", "LoadTest"]