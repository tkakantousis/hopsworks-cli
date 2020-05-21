package io.hops.cli.action;

import io.hops.cli.config.HopsworksAPIConfig;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.apache.http.HttpHeaders.USER_AGENT;

public class JobStopAction extends JobAction {
  private static final Logger logger = LoggerFactory.getLogger(JobStopAction.class);

  public JobStopAction(HopsworksAPIConfig hopsworksAPIConfig, String jobName) {
    super(hopsworksAPIConfig, jobName);
  }
  
  
  @Override
  public int execute() throws Exception {
    CloseableHttpClient getClient = getClient();
    String uri = hopsworksAPIConfig.getProjectUrl() + "/jobs/" + jobName + "/executions/stop";
    HttpPost request = new HttpPost(uri);
    request.addHeader("User-Agent", USER_AGENT);
    request.addHeader("Authorization", "ApiKey " + hopsworksAPIConfig.getApiKey());
    CloseableHttpResponse response = getClient.execute(request); //, localContext
    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
    StringBuilder result = new StringBuilder();
    String line = "";
    while ((line = rd.readLine()) != null) {
      result.append(line);
    }
    System.out.println("stopped job: " + jobName);
    response.close();
    return response.getStatusLine().getStatusCode();
  }
}
