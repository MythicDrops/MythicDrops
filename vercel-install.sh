#!/bin/bash
if [[ $VERCEL_GIT_COMMIT_REF == "9.x"  ]] ; then
  cd docs
  pnpm install
else
  cd website
  yarn install
fi
