const core = require('@actions/core');

async function run() {
  try {
    core.run("npm ci")
  } catch (error) {
    core.setFailed(error.message);
  }
}

run()
