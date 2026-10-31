const baseUrl = (import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api').replace(/\/$/, '');

export type FailureType = { code: string; description: string };
export type Options = { methods: string[]; providers: string[]; failureTypes: FailureType[] };
export type Transaction = { transactionId: string; amount: number; method: string; provider: string; status: string; failureReason: string | null; timestamp: string; failureType?: string | null; retryOutcome?: string | null };
export type PaymentInput = { amount: number; method: string; provider: string; userId: string };
export type FailureInput = { amount: number; method: string; provider: string; failureType: string; retrySucceeds: boolean };
type ApiError = { message?: string };

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  let response: Response;
  try { response = await fetch(`${baseUrl}${path}`, { ...init, headers: { 'Content-Type': 'application/json', ...init?.headers } }); }
  catch { throw new Error(`Cannot reach the backend at ${baseUrl}. Start Spring Boot and try again.`); }
  const payload: unknown = await response.json().catch(() => ({}));
  if (!response.ok) {
    const body = payload as ApiError & Partial<Transaction>;
    if (body.transactionId) throw Object.assign(new Error(body.failureReason || `Payment ${body.status?.toLowerCase() || 'failed'}.`), { transaction: body as Transaction });
    throw new Error(body.message || `Request failed (${response.status}).`);
  }
  return payload as T;
}
export const api = {
  options: () => request<Options>('/options'),
  transactions: () => request<Transaction[]>('/transactions'),
  transaction: (id: string) => request<Transaction>(`/transactions/${encodeURIComponent(id)}`),
  payment: (input: PaymentInput) => request<Transaction>('/payments', { method: 'POST', body: JSON.stringify(input) }),
  failure: (input: FailureInput) => request<Transaction>('/failures', { method: 'POST', body: JSON.stringify(input) })
};
