import * as core from "@actions/core";

async function run() {
  try {
    core.run("npm i");
  } catch (error) {
    core.setFailed(error.message);
  }
}

run().then();
