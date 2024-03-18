package mdt.docker;


/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public record DockerImageId(String repository, String tag) {
	public static DockerImageId parse(String imageId) {
		String[] parts = imageId.split(":");
		if ( parts.length == 2 ) {
			return new DockerImageId(parts[0], parts[1]);
		}
		else if ( parts.length == 1 ) {
			return new DockerImageId(parts[0], "latest");
		}
		else {
			throw new RuntimeException("invalid docker image id: " + imageId);
		}
	}
	
	public String getId() {
		return this.repository + ":" + this.tag;
	}
	
	@Override
	public String toString() {
		return getId();
	}
}
