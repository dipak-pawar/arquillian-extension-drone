package org.jboss.arquillian.drone.webdriver.binary.downloading.source;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.jboss.arquillian.drone.webdriver.binary.downloading.ExternalBinary;
import org.jboss.arquillian.drone.webdriver.utils.HttpUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.jboss.arquillian.drone.webdriver.binary.downloading.source.DummyRepositoryGitHubSource.BASE_API_URL;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpUtils.class)
public class GitHubSourceTestCase {

    private static final String BASIC_PATH =
        "src/test/resources/files/downloading/github/".replace("/", File.separator);

    @Before
    public void setMock() throws IOException {
        PowerMockito.mockStatic(HttpUtils.class);
        mockForRequest("releases/latest", BASIC_PATH + "dummy-repo-latest");
        mockForRequest("releases", BASIC_PATH + "dummy-repo-releases");
    }

    private void mockForRequest(String query, String file) throws IOException {
        BDDMockito.when(HttpUtils.sentGetRequest(BASE_API_URL + query + "?page=1")).thenReturn(
            FileUtils.readFileToString(new File(file), "utf-8"));
        BDDMockito.when(HttpUtils.sentGetRequest(BASE_API_URL + query + "?page=2")).thenReturn("[]");
    }

    @Test
    public void testGetLatestRelease() throws Exception {
        DummyRepositoryGitHubSource testRepositoryGitHubSource = new DummyRepositoryGitHubSource();
        ExternalBinary latestRelease = testRepositoryGitHubSource.getLatestRelease();

        Assertions.assertThat(latestRelease.getUrl()).isEqualTo(DummyRepositoryGitHubSource.URL_TO_LATEST_RELEASE);
        Assertions.assertThat(latestRelease.getVersion()).isEqualTo(DummyRepositoryGitHubSource.LATEST_RELEASE);
    }

    @Test
    public void testGetReleaseForVersion() throws Exception {
        String expectedRelease = "3.0.0.Final";
        DummyRepositoryGitHubSource testRepositoryGitHubSource = new DummyRepositoryGitHubSource();
        ExternalBinary releaseForVersion = testRepositoryGitHubSource.getReleaseForVersion(expectedRelease);

        String expectedUrl = String.format(DummyRepositoryGitHubSource.BASE_URL_TO_RELEASE, expectedRelease);
        Assertions.assertThat(releaseForVersion.getUrl()).isEqualTo(expectedUrl);
        Assertions.assertThat(releaseForVersion.getVersion()).isEqualTo(expectedRelease);
    }

    @Test
    public void testNonExistingVersion() throws Exception {
        String nonExisting = "non-existing";
        DummyRepositoryGitHubSource testRepositoryGitHubSource = new DummyRepositoryGitHubSource();
        ExternalBinary releaseForVersion = testRepositoryGitHubSource.getReleaseForVersion(nonExisting);
        Assertions.assertThat(releaseForVersion).isNull();
    }
}
