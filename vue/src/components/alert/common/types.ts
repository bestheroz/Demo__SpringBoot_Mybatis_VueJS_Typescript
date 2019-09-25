import { ApiDataResult } from '@/utils/api';

export interface AlertOptions {
  color?: string;
  position?: string;
  result?: ApiDataResult<any>;
  snackbar: boolean;
  timeout?: number;
  text?: string;
}
