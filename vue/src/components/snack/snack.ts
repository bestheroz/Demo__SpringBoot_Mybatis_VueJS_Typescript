import { Snack, SnackQueueState } from '@/components/snack/types';

const state = {
  snackQueue: [] as Snack[],
} as SnackQueueState;

const getters = {
  queueSize: (state: SnackQueueState) => {
    return state.snackQueue.length;
  },
  nextSnack: (state: SnackQueueState) => () => {
    return state.snackQueue.length <= 0
      ? Object.assign({})
      : state.snackQueue.shift();
  },
};

const mutations = {
  pushSnack(state: SnackQueueState, snack: Snack) {
    state.snackQueue.push(snack);
  },
};

export default {
  state,
  getters,
  mutations,
};
