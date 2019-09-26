export interface Snack {
  color: string;
  text: string;
}

export interface SnackQueueState {
  snackQueue: Array<Snack>;
}
